package com.github.zachmeyner.listeners;



import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;


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

        int yesCount = 0;
        int noCount = 0;

        // ! WHY DOES GETCOUNT RETURN ZERO ON NULL??? IT SHOULD RETURN 0!!!! THAT MEANS THERES ZERO OF THEM
        assert msg != null;
        try {
            yesCount = msg.getReaction(this.yes).getCount();
        } catch (NullPointerException ignored) {}

        try {
            noCount = msg.getReaction(this.no).getCount();
        } catch (NullPointerException ignored) {}

        System.out.println("yes: " + yesCount + "\nNo: " + noCount);

    }


}
