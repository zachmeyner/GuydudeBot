package com.github.zachmeyner.database;

import com.github.zachmeyner.Shared;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

// TODO: Documentation code

public class ServerHandler {
    private Connection db;

    public ServerHandler() {
        String dbURL = Shared.dotenv.get("DATABASE");
        dbURL = dbURL + Shared.dotenv.get("DBNAME");
        String user = Shared.dotenv.get("DBUSER");
        String pass = Shared.dotenv.get("PASS");
        try {
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

    public void CreateDefaultEntry(long channelID, long serverID, long webhookID, String webhookToken) {
        String query = "INSERT INTO Servers VALUES (" + serverID + ", 6, " + channelID + ", " + webhookID + ", "
                + "'" + webhookToken + "');";


        try {
            Statement st = this.db.createStatement();
            st.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("Failed in CreateDefaultDirectory\n" + e);
        }
    }

    public void DeleteEntry(long serverID) {
        String query = "DELETE FROM Servers WHERE id = " + serverID + ';';

        try {
            Statement st = this.db.createStatement();
            st.executeUpdate(query);

        } catch (Exception e) {
            System.out.println("Failed in DeleteEntry\n" + e);
        }
    }

    public void ChangePinCount(long serverID, int pinCount) {
        String query = "UPDATE Servers SET pin_count = " + pinCount + " WHERE id = " + serverID + ';';

        try {
            Statement st = this.db.createStatement();
            st.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("Failed in ChangePinCount\n" + e);
        }
    }
}
