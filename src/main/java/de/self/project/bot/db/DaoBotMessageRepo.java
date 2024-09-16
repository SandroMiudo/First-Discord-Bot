package de.self.project.bot.db;

import de.self.project.bot.logic.message.BotMessage;
import org.springframework.data.repository.CrudRepository;

public interface DaoBotMessageRepo extends CrudRepository<BotMessage ,Integer>{

}
