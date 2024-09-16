package de.self.project.bot.services;

import de.self.project.bot.db.MessageEntity;
import de.self.project.bot.logic.message.MessageData;
import de.self.project.bot.logic.message.MessageText;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    private MessageRepo messageRepo;
    private MessageRepoV_Bad messageRepoV_bad;

    public MessageService(MessageRepo messageRepo, MessageRepoV_Bad messageRepoV_bad) {
        this.messageRepo = messageRepo;
        this.messageRepoV_bad = messageRepoV_bad;
    }

    public void addBadWord(String word){
        messageRepoV_bad.addWord(word);
    }

    public void loadListOfBadWords(List<String> words){
        words.forEach(this::addBadWord);
    }

    public void addMessage(User user,Message message){
        MessageData messageData = messageRepoV_bad.retrieveAllWords();
        MessageText messageText = new MessageText(message);
        Message fineMessage = messageText.analyzeMessage(messageData);
        if(fineMessage == null){
            return;
        }
        messageRepo.addMessage(user,fineMessage);
    }

    public List<MessageEntity> loadMessages(User user){
        return messageRepo.getMessagesOf(user);
    }

    public boolean checkSpam(User user) {
        List<MessageEntity> messageEntities = loadMessages(user);
        if(messageEntities.size() < 10){
            return false;
        }
        // get the 10 latest messages and look if they are close to each
        List<MessageEntity> subMessages = messageEntities.subList(messageEntities.size()-10,messageEntities.size()-1);
        LocalDateTime timeToCompare = subMessages.get(0).getTime();
        for(int i = 1; i < subMessages.size(); i++){
            LocalDateTime compare = subMessages.get(i).getTime();
            if(timeToCompare.plusSeconds(5).isAfter(compare)){
                timeToCompare = compare;
                continue;
            }
            return false;
        }
        return true;
    }
}
