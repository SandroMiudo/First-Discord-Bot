package de.self.project.bot.services;

import de.self.project.bot.logic.message.BotMessage;

import java.util.List;

public interface BotMessageRepo {
    public void initMessages(List<BotMessage> messages);
    public BotMessage getRandomMessage();
}
