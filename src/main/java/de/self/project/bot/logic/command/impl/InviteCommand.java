package de.self.project.bot.logic.command.impl;

import de.self.project.bot.build.helpers.ROLES;
import de.self.project.bot.logic.command.GenericCommand;
import de.self.project.bot.logic.command.ICommandHandler;
import de.self.project.bot.logic.command.ICommandProperties;
import de.self.project.bot.logic.command.IPermission;
import de.self.project.bot.logic.message.MessageText;
import de.self.project.bot.util.PermissionUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.time.LocalDateTime;
import java.util.List;

public class InviteCommand extends GenericCommand {

    private final ICommandProperties commandProperties;
    private LocalDateTime timeToCommand = null;

    public InviteCommand(ICommandProperties commandProperties, IPermission iPermission, ICommandHandler commandHandler) {
        super(commandProperties, iPermission, commandHandler);
        this.commandProperties = commandProperties;
    }

    @Override
    public boolean validateCommand(MessageText message, List<User> allUsersInGuild) {
        return false;
    }


    @Override
    public boolean access(Guild guild, Member member) {
        return member.getRoles().stream().anyMatch(x -> x.hasPermission(PermissionUtil.MEMBER_PERMISSION()) ||
                x.hasPermission(Permission.ADMINISTRATOR) || x.hasPermission(PermissionUtil.CO_OWNER_PERMISSION()));
    }

    @Override
    public void execute(Guild guild, MessageText messageText) {

    }

    @Override
    public void timeTillCommand(LocalDateTime localDateTime) {
        this.timeToCommand = localDateTime;
    }

    @Override
    public String getCommandNameRaw() {
        return commandProperties.getProperty();
    }

    @Override
    public String description() {
        return "-InviteCommand Usage : [Prefix+<invite>] [Username:Discriminator]";
    }

    @Override
    public String logMessage(MessageText message, User user, LocalDateTime time) {
        return null;
    }
}
