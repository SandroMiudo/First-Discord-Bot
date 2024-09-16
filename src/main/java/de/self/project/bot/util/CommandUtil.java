package de.self.project.bot.util;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.util.List;
import java.util.Optional;

public class CommandUtil {

    public static User getUserFromCommand(String [] name, Guild guild){
        return retrieveUser(name, guild.getMembers());
    }

    private static User retrieveUser(String [] name, List<Member> membersInGuild){
        Optional<User> opt = membersInGuild.stream().map(Member::getUser).filter(x -> x.getName().equals(name[0]) &&
                x.getDiscriminator().equals(name[1])).findFirst();
        return opt.orElse(null);
    }

    public static boolean userInGuild(String username, List<User> allUsersInGuild){
        return allUsersInGuild.stream().anyMatch(x -> (x.getName() +":"+ x.getDiscriminator()).equals(username));
    }

    public static User retrieveBannedUser(String [] name , Guild guild){
        String s = name[0]+":"+name[1];
        Optional<User> opt = guild.retrieveBanList().stream().map(Guild.Ban::getUser)
                .filter(x -> (x.getName() + ":" + x.getDiscriminator()).equals(s)).findFirst();
        return opt.orElse(null);
    }

}
