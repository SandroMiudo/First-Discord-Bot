package de.self.project.bot.logic;

import net.dv8tion.jda.api.entities.*;

import java.util.List;
import java.util.Map;

public class DiscordMember {
    private User user;

    public DiscordMember(User user) {
        this.user = user;
    }

    public User discordUser(){
        return user;
    }

    public String retrieveName(){
        return String.format("%s%s%s",user.getName(),":",user.getDiscriminator());
    }

    public void grantRoles(DiscordGuild guild, Map.Entry<Member,List<Role>> mapping){
        guild.addRolesToMember(this, mapping.getValue());
    }

    public Member retrieveMember(DiscordGuild discordGuild){
        return discordGuild.getMember(this);
    }
}
