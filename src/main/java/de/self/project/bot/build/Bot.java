package de.self.project.bot.build;

import de.self.project.bot.exception.ConfigurationException;
import de.self.project.bot.logic.message.BotMessage;
import de.self.project.bot.services.BotService;
import net.dv8tion.jda.api.JDA;

import java.util.List;

public class Bot {
    private BotSettings botSettings;
    private JDA jda;
    private BotConfigs configs;
    private BotService botService;

    Bot(BotService botService, BotSettings botSettings, JDA jda, BotConfigs configs) {
        this.botService = botService;
        this.botSettings = botSettings;
        this.jda = jda;
        this.configs = configs;
    }

    public void init() throws ConfigurationException {
        botService.initBot(botSettings,jda,configs);
    }
}