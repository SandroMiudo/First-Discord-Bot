package de.self.project.bot.db;

import de.self.project.bot.logic.DiscordMember;
import de.self.project.bot.services.UserRepo;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class UserRepoImpl implements UserRepo {

    private DaoUserRepo daoUserRepo;

    public UserRepoImpl(DaoUserRepo daoUserRepo) {
        this.daoUserRepo = daoUserRepo;
    }

    @Override
    public void saveUser(DiscordMember discordMember) {
        String s = discordMember.retrieveName();
        String[] name_has = s.split(":");
        Member member = new Member(name_has[0],name_has[1]);
        daoUserRepo.save(member);
    }

    @Override
    public void deleteUser(DiscordMember discordMember) {

    }

    @Override
    public List<DiscordMember> getAllMembers(Guild guild) {
        List<DiscordMember> d = new ArrayList<>();
        daoUserRepo.findAll().forEach(x -> d.add(new DiscordMember(
                Objects.requireNonNull(guild.getMemberByTag(x.getName(), x.getDiscriminator())).getUser())));
        return d;
    }
}
