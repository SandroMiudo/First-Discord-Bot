package de.self.project.bot.db;

import de.self.project.bot.services.MessageRepo;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class MessageRepoImpl implements MessageRepo {

    DaoMessageRepo daoMessageRepo;

    public MessageRepoImpl(DaoMessageRepo daoMessageRepo) {
        this.daoMessageRepo = daoMessageRepo;
    }

    @Override
    public void addMessage(User user, Message message) {
        Member member = new Member(user.getName(),user.getDiscriminator());
        MessageEntity messageEntity =
                new MessageEntity(message.getContentRaw(),member.getName(),member.getDiscriminator(), LocalDateTime.now());
        daoMessageRepo.save(messageEntity);
    }



    @Override
    public List<MessageEntity> getMessagesOf(User user) {
        return daoMessageRepo.findByNameAndDiscriminator(user.getName(),user.getDiscriminator());
    }
}
