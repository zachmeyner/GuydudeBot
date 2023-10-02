package com.github.zachmeyner;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.emoji.Emoji;

/**
 * Shared variables across all classes
 */
public class Shared {
    public static Dotenv dotenv = Dotenv.load();
    public static Emoji yes = Emoji.fromUnicode("U+2705");
    public static Emoji no = Emoji.fromUnicode("U+274C");
}
