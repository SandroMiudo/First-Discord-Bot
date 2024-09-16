package de.self.project.bot.db;

import de.self.project.bot.logic.message.MessageData;
import de.self.project.bot.services.MessageRepoV_Bad;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MessageRepoV_Impl implements MessageRepoV_Bad {

    DaoMessageV2_BadRepo daoMessageV2_badRepo;

    public MessageRepoV_Impl(DaoMessageV2_BadRepo daoMessageV2_badRepo) {
        this.daoMessageV2_badRepo = daoMessageV2_badRepo;
    }

    @Override
    public void addWord(String s) {
        MessageEntityBadWord messageEntityBadWord = new MessageEntityBadWord(null,s);
        daoMessageV2_badRepo.save(messageEntityBadWord);
    }

    @Override
    public void addWords(List<String> badWords) {
        daoMessageV2_badRepo.deleteAll();
        badWords.stream().map(x -> Arrays.stream(x.split("[\n]")).collect(Collectors.toList()))
                .flatMap(Collection::stream).forEach(this::addWord);
    }

    @Override
    public MessageData retrieveAllWords() {
        List<String> dbData = new ArrayList<>();
        daoMessageV2_badRepo.findAll().forEach(x -> dbData.add(x.getMessage()));
        return new MessageData(dbData);
    }
}
