package de.self.project.bot.db;

import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;

public interface DaoProtocolRepo extends CrudRepository<ProtocolEntity,Long>{
    public ProtocolEntity findByTimeIsLessThanEqual(LocalDateTime time);
}
