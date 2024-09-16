package de.self.project.bot.logic.command.impl;

import de.self.project.bot.logic.command.GenericCommand;
import de.self.project.bot.logic.command.ICommandHandler;
import de.self.project.bot.logic.command.ICommandProperties;
import de.self.project.bot.logic.command.IPermission;
import de.self.project.bot.logic.message.MessageText;
import de.self.project.bot.util.PermissionUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DelRoleCommand extends GenericCommand {

    private final ICommandProperties commandProperties;

    public DelRoleCommand(ICommandProperties commandProperties, IPermission iPermission, ICommandHandler commandHandler) {
        super(commandProperties, iPermission, commandHandler);
        this.commandProperties = commandProperties;
    }

    @Override
    public boolean access(Guild guild, Member member) {
        return member.getRoles().stream().anyMatch(x -> x.hasPermission(PermissionUtil.admin));
    }

    @Override
    public void execute(Guild guild, MessageText messageText){
        String param = messageText.convertIntoParamInput(1);
        guild.getRolesByName(param,true).get(0).delete().complete();
    }

    @Override
    public void timeTillCommand(LocalDateTime localDateTime) {

    }

    @Override
    public String getCommandNameRaw() {
        return commandProperties.getProperty();
    }

    @Override
    public String description() {
        return "-DelRoleCommand Usage : [Prefix+<role_del>] + [Role_Name]";
    }

    @Override
    public String logMessage(MessageText message, User user, LocalDateTime time) {
        return String.format("Role %s was deleted by %s at %s\n",
                message.convertIntoParamInput(1),
                user.getName(),
                time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    @Override
    public boolean validateCommand(MessageText message, List<User> allUsersInGuild) {
        if(!correctSize(message.getContentRaw())) return false;
        return correctRole(message.getMessageRaw(), message.retrieveGuild());
    }

    private boolean correctSize(String messageRaw){
        return messageRaw.split("[ ]").length == 2;
    }

    private boolean correctRole(String messageRaw,Guild g){
        String role = messageRaw.split("[ ]")[1];
        return g.getRoles().stream().anyMatch(x -> x.getName().equalsIgnoreCase(role));
    }
}
