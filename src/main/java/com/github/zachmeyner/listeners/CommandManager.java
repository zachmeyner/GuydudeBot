package com.github.zachmeyner.listeners;

import com.github.zachmeyner.builders.WebhookBuilder;
import com.github.zachmeyner.database.PinHandler;
import com.github.zachmeyner.database.ServerHandler;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class CommandManager extends ListenerAdapter {

    private final List<CommandData> commands = new ArrayList<>();

    public CommandManager() {
        setupCommands();
    }

    private void setupCommands() {
        this.commands.add(Commands.slash("set-pin-channel", "Set channel for pinned messages to go into")
                .addOption(OptionType.CHANNEL, "channel", "Channel for pinned messages to go into",
                        true)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)));


    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        super.onSlashCommandInteraction(event);

        String command = event.getName();
        if (command.equals("set-pin-channel")) {
            event.deferReply().queue();

            GuildChannel chn = Objects.requireNonNull(event.getOption("channel")).getAsChannel();
            if (!chn.getType().equals(ChannelType.TEXT)) {
                event.getHook().sendMessage("Channel must be a text channel").queue();
                return;
            }

            if (!CheckSetup(chn)) {
                SetupServer(chn);
                event.getHook().sendMessage("First time setup complete, pin channel set to <#" + chn.getIdLong()
                                + '>').queue();

            } else {
                NewChannel(chn);
                event.getHook().sendMessage("Pin channel set to <#" + chn.getIdLong()
                        + '>').queue();
            }
        }

    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        super.onGuildReady(event);

        event.getGuild().updateCommands().addCommands(this.commands).queue();
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        super.onGuildJoin(event);

        event.getGuild().updateCommands().addCommands(this.commands).queue();
    }

    private void SetupServer(GuildChannel chn) {
        ServerHandler db = new ServerHandler();

        long channelID = chn.getIdLong();
        long serverID = chn.getGuild().getIdLong();

        Pair<Long, String> tmp = WebhookBuilder.CreateWebhook(channelID, chn.getGuild());

        db.CreateDefaultEntry(channelID, serverID, tmp.getValue0(), tmp.getValue1());

        db.closeDB();
    }

    private boolean CheckSetup(GuildChannel chn) {
        PinHandler db = new PinHandler();

        if (db.CheckForServer(chn.getGuild().getIdLong())) {
            db.closeDB();
            return true;
        }

        db.closeDB();
        return false;
    }

    private void NewChannel(GuildChannel chn) {
        long oldID;

        PinHandler db0 = new PinHandler();

        oldID = db0.GetPinChannel(chn.getGuild().getIdLong());

        ServerHandler db = new ServerHandler();

        db.DeleteEntry(chn.getGuild().getIdLong());
        db.closeDB();

        TextChannel txt = chn.getGuild().getTextChannelById(oldID);

        assert txt != null;
        txt.deleteWebhookById(txt.retrieveWebhooks().complete().get(0).getId()).queue();

        SetupServer(chn);
    }

}
