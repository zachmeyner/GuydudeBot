package com.github.zachmeyner.listeners;



import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class EventListener extends ListenerAdapter {

    private int requiredCount = 6;
    private Emoji yes = Emoji.fromUnicode("U+2705");
    private Emoji no = Emoji.fromUnicode("U+274C");


    @Override
    public void onGenericMessageReaction(@NotNull GenericMessageReactionEvent event) {
        super.onGenericMessageReaction(event);

        MessageChannel chn = event.getGuildChannel();
        MessageHistory hist = chn.getHistoryFromBeginning(100).complete();
        Message msg = hist.getMessageById((event.getMessageIdLong()));

        assert msg != null;
        int yesCount = Objects.requireNonNull(msg.getReaction(this.yes)).getCount();
        int noCount = Objects.requireNonNull(msg.getReaction(this.no)).getCount();

        System.out.println("yes: " + yesCount + "\nNo: " /*+ noCount*/);

    }


}
