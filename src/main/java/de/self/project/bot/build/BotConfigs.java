package de.self.project.bot.build;

import de.self.project.bot.logic.message.BotMessage;

import java.util.List;

public class BotConfigs {

    private List<String> badWords;
    private List<String> rules;
    private List<BotMessage> botMessages;

    public BotConfigs(List<String> badWords, List<String> rules, List<BotMessage> botMessages) {
        this.badWords = badWords;
        this.rules = rules;
        this.botMessages = botMessages;
    }

    public List<String> getBadWords() {
        return badWords;
    }

    public List<String> getRules() {
        return rules;
    }

    public List<BotMessage> getBotMessages() {
        return botMessages;
    }
}
