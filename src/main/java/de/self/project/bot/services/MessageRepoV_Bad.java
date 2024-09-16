package de.self.project.bot.services;

import de.self.project.bot.logic.message.MessageData;

import java.util.List;

public interface MessageRepoV_Bad {
    void addWord(String s);
    void addWords(List<String> badWords);
    MessageData retrieveAllWords();
}
