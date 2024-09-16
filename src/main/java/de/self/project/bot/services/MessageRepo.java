package de.self.project.bot.services;

import de.self.project.bot.db.MessageEntity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public interface MessageRepo{
    void addMessage(User user, Message message);
    List<MessageEntity> getMessagesOf(User user);
}
