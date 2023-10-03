package com.github.zachmeyner.builders;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.github.zachmeyner.types.PinType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.javatuples.Pair;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

// TODO: Documentation code

public class WebhookBuilder {

    private final WebhookClient client;

    public WebhookBuilder(long id, String token) {
        WebhookClientBuilder builder = new WebhookClientBuilder(id, token);
        builder.setThreadFactory((job) -> {
            Thread thread = new Thread(job);
            thread.setName("Hello");
            thread.setDaemon(true);
            return thread;
        });
        builder.setWait(true);
        this.client = builder.build();
    }

    public void PostLit(Message msg, PinType type) {
        List<Message.Attachment> attachments = msg.getAttachments();
        WebhookEmbedBuilder embedBuilder;

        if (type == PinType.LIT) {
            embedBuilder = new WebhookEmbedBuilder()
                    .setTitle(new WebhookEmbed.EmbedTitle(":white_check_mark: This goes Hard :white_check_mark:", null))
                    .setDescription("[Link](" + msg.getJumpUrl() + ")");
        } else {
            embedBuilder = new WebhookEmbedBuilder()
                    .setTitle(new WebhookEmbed.EmbedTitle(":x: This does not Hard :x:", null))
                    .setDescription("[Link](" + msg.getJumpUrl() + ")");

        }

        WebhookEmbed embed = embedBuilder.build();


        if (!attachments.isEmpty()) {
            List<File> fsList = new ArrayList<>();
            for (int i = 0; i < attachments.size(); i++) {
                String ext = attachments.get(i).getFileExtension();
                fsList.add(new File("temp/tmp" + i + '.' + ext));
                attachments.get(i).getProxy().downloadToFile(fsList.get(i)).join();
            }

            try {
                WebhookMessageBuilder builder = new WebhookMessageBuilder()
                        .setUsername(msg.getAuthor().getName())
                        .setAvatarUrl(msg.getAuthor().getAvatarUrl())
                        .setContent(msg.getContentDisplay())
                        .addEmbeds(embed);

                for (File fs : fsList) {
                    builder.addFile(fs);
                }
                client.send(builder.build());
                return;
            } catch (Exception ignore) {}
        }

        try {
            WebhookMessageBuilder builder = new WebhookMessageBuilder()
                    .setUsername(msg.getAuthor().getName())
                    .setAvatarUrl(msg.getAuthor().getAvatarUrl())
                    .setContent(msg.getContentDisplay())
                    .addEmbeds(embed);
            client.send(builder.build());
        } catch (Exception ignore) {}
    }


    public static Pair<Long, String> CreateWebhook(long chn, Guild guild) {
        TextChannel txt = guild.getTextChannelById(chn);
        assert txt != null;
        Webhook wh = txt.createWebhook("Pinhook").complete();
        long whID = wh.getIdLong();
        String whToken = wh.getToken();

        return new Pair<>(whID, whToken);
    }
}
