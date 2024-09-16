package de.self.project.bot.build;

import de.self.project.bot.build.command.ICommand;
import de.self.project.bot.build.helpers.Channel;
import de.self.project.bot.build.helpers.Role;
import net.dv8tion.jda.api.Permission;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class BotSettings {

    private Map<Role,List<Permission>> allRoles;
    private String guildID;
    private List<ICommand> commands;
    private List<Channel> channels;
    private Stream<String> members;

    public BotSettings(Stream<String> members, Map<Role,List<Permission>> roles, String guildID, List<ICommand> commands
                       , List<Channel> channels) {
        this.members = members;
        this.allRoles = roles;
        this.guildID = guildID;
        this.commands = commands;
        this.channels = channels;
    }

    public BotSettings(Map<Role, List<Permission>> allRoles, String guildID, List<ICommand> commands,List<Channel> channels) {
        this.allRoles = allRoles;
        this.guildID = guildID;
        this.commands = commands;
        this.channels = channels;
    }

    public List<ICommand> getCommands() {
        return commands;
    }

    public boolean isMembersPresent(){
        return members != null;
    }

    public Stream<String> getMembers() {
        return members;
    }

    public Map<Role,List<Permission>> getAllRoles() {
        return allRoles;
    }

    public String getGuildID() {
        return guildID;
    }

    public List<Channel> getChannels() {
        return channels;
    }
}
