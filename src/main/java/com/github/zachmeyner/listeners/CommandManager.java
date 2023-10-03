package com.github.zachmeyner.listeners;

import com.github.zachmeyner.builders.WebhookBuilder;
import com.github.zachmeyner.database.ServerHandler;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.github.zachmeyner.builders.WebhookBuilder.CreateWebhook;

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
            SetupServer(event);
        }

    }

    private void SetupServer(@NotNull SlashCommandInteractionEvent event) {
        ServerHandler db = new ServerHandler();

        OptionMapping messOpt = event.getOption("channel");

        assert messOpt != null;
        long channelID = messOpt.getAsChannel().getIdLong();
        long serverID = Objects.requireNonNull(event.getGuild()).getIdLong();

        Pair<Long, String> tmp = WebhookBuilder.CreateWebhook(channelID, event.getGuild());

        db.CreateDefaultEntry(channelID, serverID, tmp.getValue0(), tmp.getValue1());

        db.closeDB();
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

}
