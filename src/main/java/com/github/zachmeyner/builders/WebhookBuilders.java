package com.github.zachmeyner.builders;

import com.github.zachmeyner.types.PinType;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.javatuples.Pair;

public class WebhookBuilders {

    // TODO: Create webhook and post to pin channel
    public static void PostLit(Message msg, PinType type, long channelID, long whID, String whToken) {
        System.out.println("test commiit");

    }


    public static Pair<Long, String> CreateWebhook(long chn, Message dummy) {
        TextChannel txt = dummy.getGuild().getTextChannelById(chn);
        assert txt != null;
        Webhook wh = txt.createWebhook("Pinhook").complete();
        long whID = wh.getIdLong();
        String whToken = wh.getToken();

        return new Pair<>(whID, whToken);
    }
}
