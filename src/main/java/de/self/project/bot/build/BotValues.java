package de.self.project.bot.build;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

public class BotValues {
    private Activity activity;
    private OnlineStatus onlineStatus;
    private String name;

    public BotValues(Activity activity, OnlineStatus onlineStatus, String name) {
        this.activity = activity;
        this.onlineStatus = onlineStatus;
        this.name = name;
    }
}
