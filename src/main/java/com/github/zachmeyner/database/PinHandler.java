package com.github.zachmeyner.database;

import com.github.zachmeyner.Shared;
import org.javatuples.Pair;

import javax.swing.plaf.nimbus.State;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class PinHandler {
    private Connection db;

    public PinHandler() {
        String dbURL = Shared.dotenv.get("DATABASE");
        String user = Shared.dotenv.get("DBUSER");
        String pass = Shared.dotenv.get("PASS");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.db = DriverManager.getConnection(dbURL, user, pass);
            // System.out.println("DB Success!\n");
        } catch (Exception e) {
            System.out.println("Failed in PinHandler\n" + e);
        }
    }

    public void closeDB() {
        try {
            db.close();
        } catch (Exception ignored) {
        }
    }

    public void WritePinCount(long messageID, long serverID, int litCount, int notLitCount) {
        String query = "SELECT * FROM Messages WHERE id = " + messageID + " AND server_id = " + serverID + ';';

        try {
            Statement st = this.db.createStatement();
            ResultSet set = st.executeQuery(query);

            if (!set.isBeforeFirst()) {
                WriteNewMessage(messageID, serverID, litCount, notLitCount);
            } else {
                UpdateMessage(messageID, serverID, litCount, notLitCount);
            }
        } catch (Exception e) {
            System.out.println("Failed in WritePinCount\n" + e);
        }
    }

    private void WriteNewMessage(long messageID, long serverID, int litCount, int notLitCount) {
        String query = "INSERT INTO Messages VALUES (" + messageID + ", " + serverID + ", " + litCount + ", "
                + notLitCount + ", " + 0 + ");";

        try {
            Statement st = this.db.createStatement();
            st.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("Failed in WriteNewMessage\n" + e);
        }
    }

    private void UpdateMessage(long messageID, long serverID, int litCount, int notLitCount) {
        String query = "UPDATE Messages SET hard_reactions = " + litCount + ", not_hard_reactions = " + notLitCount
                + " WHERE id = " + messageID + " AND server_id = " + serverID + ';';

        try {
            Statement st = this.db.createStatement();
            st.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("Failed in UpdateMessage\n" + e);
        }
    }

    public boolean CheckPinned(long messageID, long serverID){
        String query = "SELECT pinned FROM Messages WHERE id = " + messageID + " AND server_id = " + serverID + ';';

        try {
            Statement st = this.db.createStatement();
            ResultSet set = st.executeQuery(query);
            set.next();
            // System.out.println("Success in CheckPinned");
            return set.getBoolean(1);
        } catch (Exception e) {
            System.out.println("Failed in CheckPinned\n" + e);
        }

        return false;
    }

    public void MarkMessagePinned(long messageID, long serverID) {
        String query = "UPDATE Messages SET pinned = 1 WHERE id = " + messageID + " AND server_id = " + serverID + ';';

        try {
            Statement st = this.db.createStatement();
            st.executeUpdate(query);
            // System.out.println("Success in MarkMessagePinned");
        } catch (Exception e) {
            System.out.println("Failed in MarkMessagePinned\n" + e);
        }
    }

    public int GetReqPinCount(long serverID) {
        String query = "SELECT pin_count FROM Servers WHERE id = " + serverID + ';';
        int rv = -1;

        try {
            Statement st = this.db.createStatement();
            ResultSet rs = st.executeQuery(query);
            rs.next();
            rv = rs.getInt(1);
            // System.out.println("Success in GetReqPinCount");
            return rv;
        } catch (Exception e) {
            System.out.println("Failed in GetReqPinCount\n" + e);
        }

        return rv;
    }

    public long GetPinChannel(long serverID) {
        String query = "SELECT pin_channel_id FROM Servers WHERE id = " + serverID + ';';
        long rv = -1;

        try {
            Statement st = this.db.createStatement();
            ResultSet rs = st.executeQuery(query);
            rs.next();
            // System.out.println("Success in GetPinChannel");
            rv = rs.getLong(1);
            return rv;
        } catch (Exception e) {
            System.out.println("Failed in GetPinChannel\n" + e);
        }

        return rv;
    }

    public boolean CheckForServer(long serverID) {
        String query = "SELECT * FROM Servers WHERE id = " + serverID + ';';

        try {
            Statement st = this.db.createStatement();
            ResultSet rs = st.executeQuery(query);
            return rs.isBeforeFirst();
        } catch (Exception e) {
            System.out.println("Failed in CheckForServer\n" + e);
        }
        return false;
    }

    public Pair<Long, String> GetWebhookInfo(long serverID) {
        String query = "SELECT webhook_id, webhook_token FROM Servers WHERE id = " + serverID + ';';
        Pair<Long, String> rv = new Pair<>((long) -1, "");

        try {
            Statement st = this.db.createStatement();
            ResultSet rs = st.executeQuery(query);
            rs.next();
            rv = Pair.with(rs.getLong(1), rs.getString(2));
            return rv;
        } catch (Exception e) {
            System.out.println("Failed in GetWebhookInfo\n" + e);
        }

        return rv;
    }
}
