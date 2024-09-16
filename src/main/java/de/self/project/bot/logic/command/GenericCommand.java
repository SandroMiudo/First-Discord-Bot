package de.self.project.bot.logic.command;

import de.self.project.bot.build.command.ICommand;
import de.self.project.bot.logic.message.MessageText;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public abstract class GenericCommand implements ICommand{

    private ICommandProperties commandProperties;
    private IPermission iPermission;
    private ICommandHandler commandHandler;

    public GenericCommand(ICommandProperties commandProperties, IPermission iPermission, ICommandHandler commandHandler) {
        this.commandProperties = commandProperties;
        this.iPermission = iPermission;
        this.commandHandler = commandHandler;
    }



    public boolean isAllowed(Member member, Message message){
        return iPermission.hasPermission(member,this) && commandHandler.compareMessage(message,this);
    }

    public abstract boolean validateCommand(MessageText message, List<User> allUsersInGuild);
}
