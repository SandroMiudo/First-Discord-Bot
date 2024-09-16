package de.self.project.bot.logic.command.impl;

import de.self.project.bot.logic.command.GenericCommand;
import de.self.project.bot.logic.command.ICommandHandler;
import de.self.project.bot.logic.command.ICommandProperties;
import de.self.project.bot.logic.command.IPermission;
import de.self.project.bot.logic.message.MessageText;
import de.self.project.bot.util.CommandUtil;
import de.self.project.bot.util.PermissionUtil;
import net.dv8tion.jda.api.entities.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TimeOutCommand extends GenericCommand {

    private final ICommandProperties commandProperties;

    public TimeOutCommand(ICommandProperties commandProperties, IPermission iPermission, ICommandHandler commandHandler) {
        super(commandProperties, iPermission, commandHandler);
        this.commandProperties = commandProperties;
    }

    @Override
    public boolean validateCommand(MessageText message, List<User> allUsersInGuild) {
        if(!correctSize(message.getContentRaw())) return false;
        if(!CommandUtil.userInGuild(message.convertIntoNameInput(1),allUsersInGuild)) return false;
        return correctInputParam(message.getContentRaw());
    }

    private boolean correctSize(String messageRaw){
        return messageRaw.split("[ ]").length == 3;
    }


    private boolean correctInputParam(String messageRaw){
        String input = messageRaw.split("[ ]")[2];
        try{
            Integer.parseInt(input);
        }
        catch (IllegalArgumentException illegalArgumentException){
            return false;
        }
        return true;
    }

    @Override
    public boolean access(Guild guild, Member member) {
        return member.getRoles().stream().anyMatch(x -> x.hasPermission(PermissionUtil.admin) ||
                x.hasPermission(PermissionUtil.CO_OWNER_PERMISSION()));
    }

    @Override
    public void execute(Guild guild, MessageText messageText) {
        String param = messageText.convertIntoParamInput(2);
        int i = Integer.parseInt(param);
        String name = messageText.convertIntoNameInput(1);
        User user = CommandUtil.getUserFromCommand(name.split("[:]"), guild);
        if(user == null){
            return;
        }
        guild.timeoutFor(UserSnowflake.fromId(user.getId()),i, TimeUnit.HOURS).complete();
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
        return "-TimeOutCommand Usage : [Prefix+<timeout>] + [Username:Discriminator] + [Time(in hours)]";
    }

    @Override
    public String logMessage(MessageText message, User user, LocalDateTime time) {
        return String.format("%s got a %s hour/s timeout by %s at %s\n",message.convertIntoNameInput(1),
                message.convertIntoParamInput(2),user.getName(),time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

}
