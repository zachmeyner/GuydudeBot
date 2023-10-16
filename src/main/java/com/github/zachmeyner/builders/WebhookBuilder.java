package com.github.zachmeyner.builders;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.github.zachmeyner.types.PinType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
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
        List<WebhookEmbed> webhookEmbeds = new ArrayList<>();




        for (MessageEmbed msgEmbed : msg.getEmbeds()) {
            embedBuilder = new WebhookEmbedBuilder()
                    .setTitle(new WebhookEmbed.EmbedTitle(
                            (msgEmbed.getTitle() == null) ? "" : msgEmbed.getTitle(),
                            msgEmbed.getUrl()
                    ))
                    .setDescription(msgEmbed.getDescription())
                    .setColor(msgEmbed.getColorRaw())
                    .setImageUrl((msgEmbed.getImage() == null) ? "" : msgEmbed.getImage().getUrl())
                    .setThumbnailUrl((msgEmbed.getThumbnail() == null) ? "" : msgEmbed.getThumbnail().getUrl())
                    .setTimestamp(msgEmbed.getTimestamp());

            if (msgEmbed.getFooter() != null) {
                embedBuilder.setFooter(new WebhookEmbed.EmbedFooter(
                        (msgEmbed.getFooter().getText() == null) ? "" : msgEmbed.getFooter().getText(),
                        msgEmbed.getFooter().getIconUrl()
                ));
            }

            if (msgEmbed.getAuthor() != null) {
                embedBuilder.setAuthor(new WebhookEmbed.EmbedAuthor(
                                (msgEmbed.getAuthor().getName() == null) ? "" : msgEmbed.getAuthor().getName(),
                                msgEmbed.getAuthor().getIconUrl(),
                                msgEmbed.getAuthor().getUrl()
                        ));
            }

            for (MessageEmbed.Field fld : msgEmbed.getFields()) {
                embedBuilder.addField(
                        new WebhookEmbed.EmbedField(
                                fld.isInline(),
                                (fld.getName() == null) ? "" : fld.getName(),
                                (fld.getValue() == null) ? "" : fld.getValue()
                        )
                );
            }

            webhookEmbeds.add(embedBuilder.build());
            embedBuilder.reset();

        }


        if (type == PinType.LIT) {
            embedBuilder = new WebhookEmbedBuilder()
                    .setTitle(new WebhookEmbed.EmbedTitle(":white_check_mark: This goes Hard :white_check_mark:", null))
                    .setDescription("[Link](" + msg.getJumpUrl() + ")")
                    .setColor(5763719);

        } else {
            embedBuilder = new WebhookEmbedBuilder()
                    .setTitle(new WebhookEmbed.EmbedTitle(":x: This does not go Hard :x:", null))
                    .setDescription("[Link](" + msg.getJumpUrl() + ")")
                    .setColor(15548997);

        }

        WebhookEmbed embed = embedBuilder.build();



            List<File> fsList = new ArrayList<>();
            for (int i = 0; i < attachments.size(); i++) {
                String ext = attachments.get(i).getFileExtension();
                fsList.add(new File("temp/tmp" + i + '.' + ext));
                attachments.get(i).getProxy().downloadToFile(fsList.get(i)).join();
            }

            try {
                WebhookMessageBuilder builder = new WebhookMessageBuilder()
                        .setUsername(msg.getAuthor().getEffectiveName())
                        .setAvatarUrl(msg.getAuthor().getEffectiveAvatarUrl())
                        .setContent(msg.getContentDisplay());

                for (File fs : fsList) {
                    builder.addFile(fs);
                }

                for (WebhookEmbed fin : webhookEmbeds) {
                    builder.addEmbeds(fin);
                }

                builder.addEmbeds(embed);

                client.send(builder.build());
            } catch (Exception ignore) {
            }

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
