package de.self.project.bot.db;

import org.springframework.data.repository.CrudRepository;

public interface DaoUserRepo extends CrudRepository<Member,Integer> {

}
