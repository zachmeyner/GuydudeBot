package com.github.zachmeyner.database;

import com.github.zachmeyner.Guydude_Bot;
import io.github.cdimascio.dotenv.Dotenv;

public class Handlers {
    private final Dotenv dotenv;
    public Handlers() {
        dotenv = Dotenv.load();
        String dbURL = dotenv.get("DATABASE");
    }
}
