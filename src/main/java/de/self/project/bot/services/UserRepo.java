package de.self.project.bot.services;

import de.self.project.bot.logic.DiscordMember;
import net.dv8tion.jda.api.entities.Guild;

import java.util.List;

public interface UserRepo{
    public void saveUser(DiscordMember discordMember);
    public void deleteUser(DiscordMember discordMember);
    public List<DiscordMember> getAllMembers(Guild guild);
}
