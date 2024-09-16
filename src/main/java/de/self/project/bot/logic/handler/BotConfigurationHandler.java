package de.self.project.bot.logic.handler;

import de.self.project.bot.exception.ConfigurationException;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

import java.util.*;

public class BotConfigurationHandler {

    public Map<Member, List<Role>> configMapping(Guild guild, Map<String,String> map) throws ConfigurationException {
        Map<Member,List<Role>> m = new HashMap<>();
        for(Map.Entry<String,String> entry : map.entrySet()){
            List<Role> roles = new ArrayList<>();
            Member member = validUser(guild, entry.getKey());
            for(String s : entry.getValue().split("[,]")){
                roles.add(findRoleByName(guild,s));
            }
            m.put(member,roles);
        }
        return m;
    }

    private Role findRoleByName(Guild g, String roleName) throws ConfigurationException {
        if(g.getRoles().stream().noneMatch(x -> x.getName().equalsIgnoreCase(roleName))){
            throw new ConfigurationException("Configuration is set false! Role does not exist!");
        }
        return g.getRoles().stream().filter(x -> x.getName().equalsIgnoreCase(roleName)).findFirst().get();
    }

    private Member validUser(Guild g, String username) throws ConfigurationException {
        String[] discordName = username.split("[:]");
        if(discordName.length != 2 ||
                g.getMembers().stream().map(Member::getUser).noneMatch(x -> x.getName().equals(discordName[0]) &&
                        x.getDiscriminator().equals(discordName[1]))){
            throw new ConfigurationException("Configuration is set false! Not a valid User!");
        }
        return Objects.requireNonNull(g.getMemberByTag(discordName[0], discordName[1]));
    }
}
