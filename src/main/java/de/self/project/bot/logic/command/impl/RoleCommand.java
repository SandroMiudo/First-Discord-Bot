package de.self.project.bot.logic.command.impl;

import de.self.project.bot.build.helpers.ROLES;
import de.self.project.bot.logic.command.GenericCommand;
import de.self.project.bot.logic.command.ICommandHandler;
import de.self.project.bot.logic.command.ICommandProperties;
import de.self.project.bot.logic.command.IPermission;
import de.self.project.bot.util.CommandUtil;
import de.self.project.bot.logic.message.MessageText;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RoleCommand extends GenericCommand {

    private final ICommandProperties commandProperties;

    public RoleCommand(ICommandProperties commandProperties, IPermission iPermission, ICommandHandler commandHandler) {
        super(commandProperties, iPermission, commandHandler);
        this.commandProperties = commandProperties;
    }

    @Override
    public boolean validateCommand(MessageText message, List<User> allUsersInGuild) {
        if(!CommandUtil.userInGuild(message.convertIntoNameInput(1),allUsersInGuild)){
            return false;
        }
        if(!hasSize(message.getContentRaw(),3)){
            return false;
        }
        return validRoleAsInput(message.getContentRaw().split("[ ]")[2]);

    }


    @Override
    public boolean access(Guild guild, Member member) {
        return member.getRoles().stream().anyMatch(x -> x.hasPermission(Permission.ADMINISTRATOR));
    }

    @Override
    public void execute(Guild guild, MessageText messageText) {
        String [] s = messageText.convertIntoNameInput(1).split("[:]");
        User user = CommandUtil.getUserFromCommand(s, guild);
        if(user == null){
            return;
        }
        String s1 = messageText.convertIntoParamInput(2);
        guild.addRoleToMember(UserSnowflake.fromId(user.getId()),guild.getRolesByName(s1,true)
                .get(0)).complete();
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
        return "-RoleCommand Usage : [Prefix+<role>] + [Username:Discriminator] + [Role-Name]";
    }

    @Override
    public String logMessage(MessageText message, User user, LocalDateTime time) {
        return String.format("role %s was added to %s by %s at %s\n",
                message.convertIntoParamInput(2),
                message.convertIntoNameInput(1),
                user.getName(),
                time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }


    private boolean hasSize(String contentRaw,int size){
        return contentRaw.split("[ ]").length == size;
    }

    private boolean validRoleAsInput(String role){
        for(ROLES r : ROLES.values()){
            if(r.name().equalsIgnoreCase(role)){
                return true;
            }
        }
        return false;
    }
}
