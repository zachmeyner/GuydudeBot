package com.github.zachmeyner.listeners;



import com.github.zachmeyner.Shared;
import com.github.zachmeyner.builders.WebhookBuilders;
import com.github.zachmeyner.database.PinHandler;
import com.github.zachmeyner.types.PinType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;



// TODO: CREATE WEBHOOKS FOR POSTS
public class PinEventListener extends ListenerAdapter {
    @Override
    public void onGenericMessageReaction(@NotNull GenericMessageReactionEvent event) {
        super.onGenericMessageReaction(event);

        if (!event.isFromGuild()) {
            return;
        }

        Message msg = event.getChannel().retrieveMessageById(event.getMessageId()).complete();
        // MessageChannel chn = event.getGuildChannel();
        // MessageHistory hist = chn.getHistoryAround(event.getMessageIdLong(), 1).complete();
        // Message msg = hist.getMessageById((event.getMessageIdLong()));


        int yesCount = 0;
        int noCount = 0;

        // ! WHY DOES message#getCount() RETURN ZERO ON NULL??? IT SHOULD RETURN 0!!!! THAT MEANS THERE'S ZERO OF THEM
        assert msg != null;
        try {
            yesCount = Objects.requireNonNull(msg.getReaction(Shared.yes)).getCount();
        } catch (NullPointerException ignored) {}

        try {
            noCount = Objects.requireNonNull(msg.getReaction(Shared.no)).getCount();
        } catch (NullPointerException ignored) {}

        long messageID = event.getMessageIdLong();
        long serverID = event.getGuild().getIdLong();

        PinHandler db = new PinHandler();

        db.WritePinCount(messageID, serverID, yesCount, noCount);

        if (db.CheckPinned(messageID, serverID)) {
            db.closeDB();
            return;
        }

        int pc = db.GetReqPinCount(serverID);
        long pinChannel = db.GetPinChannel(serverID);

        if (yesCount >= pc) {
            db.MarkMessagePinned(messageID, serverID);
            WebhookBuilders.PostLit(msg, PinType.LIT, pinChannel);

        } else if (noCount >= pc) {
            db.MarkMessagePinned(messageID, serverID);
        }

        //System.out.println("Pinned: " + db.CheckPinned(messageID, serverID));


        // System.out.println("yes: " + yesCount + "\nNo: " + noCount);
        db.closeDB();
    }
}
