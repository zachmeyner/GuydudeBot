package com.github.zachmeyner;

import com.github.zachmeyner.listeners.CommandManager;
import com.github.zachmeyner.listeners.PinEventListener;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.security.auth.login.LoginException;


/**
 * JDA Discord Bot
 * Main class for init
 *
 * @author github.com/Zachmeyner
 */


public class GuydudeBot {

    private final ShardManager shardManager;

    /**
     * Instantiation of bot
     *
     * @throws LoginException when token is invalid.
     */
    public GuydudeBot() throws LoginException {

        String token = Shared.dotenv.get("TOKEN");

        DefaultShardManagerBuilder builder;
        builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.watching("Billy Madison"));
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT);


        shardManager = builder.build();

        shardManager.addEventListener(new PinEventListener(), new CommandManager());

    }

    public static void main(String[] args) {
        try {
            GuydudeBot bot = new GuydudeBot();
            System.out.println("Bot running!");
        } catch (LoginException e) {
            System.out.println("Bad Token");
        }

    }
}

