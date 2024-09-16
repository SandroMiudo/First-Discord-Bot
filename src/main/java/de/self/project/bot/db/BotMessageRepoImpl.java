package de.self.project.bot.db;

import de.self.project.bot.logic.message.BotMessage;
import de.self.project.bot.services.BotMessageRepo;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class BotMessageRepoImpl implements BotMessageRepo{

    DaoBotMessageRepo daoBotMessageRepo;

    public BotMessageRepoImpl(DaoBotMessageRepo daoBotMessageRepo) {
        this.daoBotMessageRepo = daoBotMessageRepo;
    }

    @Override
    public void initMessages(List<BotMessage> messages) {
        daoBotMessageRepo.deleteAll();
        daoBotMessageRepo.saveAll(messages);
    }

    @Override
    public BotMessage getRandomMessage() {
        List<BotMessage> botMessages = new ArrayList<>();
        daoBotMessageRepo.findAll().forEach(botMessages::add);
        Collections.shuffle(botMessages);
        return BotMessage.retrieveRandomMessage(botMessages);
    }
}
