package com.github.zachmeyner;

import com.github.zachmeyner.listeners.PinEventListener;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.security.auth.login.LoginException;


/**
 * JDA Discord Bot
 * Main class for init
 *
 * @author github.com/Zachmeyner
 */


public class Guydude_Bot {

    private final ShardManager shardManager;

    /**
     * Instantiation of bot
     *
     * @throws LoginException when token is invalid.
     */
    public Guydude_Bot() throws LoginException {

        String token = Shared.dotenv.get("TOKEN");

        DefaultShardManagerBuilder builder;
        builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.watching("Billy Madison"));

        shardManager = builder.build();

        shardManager.addEventListener(new PinEventListener());

    }


    /**
     * @return shardManager for bot
     */
    public ShardManager GetShardManager() {
        return shardManager;
    }



    public static void main(String[] args) {
        try {
            Guydude_Bot bot = new Guydude_Bot();
            System.out.println("Bot running!");
        } catch (LoginException e) {
            System.out.println("Bad Token");
        }

    }
}

