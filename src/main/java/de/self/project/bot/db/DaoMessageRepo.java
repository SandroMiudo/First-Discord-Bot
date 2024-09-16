package de.self.project.bot.db;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DaoMessageRepo extends CrudRepository<MessageEntity,Integer> {
    public List<MessageEntity> findByNameAndDiscriminator(String name,String discriminator);
}
