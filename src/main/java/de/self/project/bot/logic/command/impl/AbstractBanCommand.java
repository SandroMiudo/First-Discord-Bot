package de.self.project.bot.logic.command.impl;

import de.self.project.bot.build.helpers.ROLES;
import de.self.project.bot.logic.command.*;
import de.self.project.bot.logic.message.MessageText;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


// Command of type : !ban user time (first Command when banning)
// !ban user when user already got banned one time
// *time -> BAN_DURATION

public abstract class AbstractBanCommand extends GenericCommand {
    private final ICommandProperties commandProperties;
    private LocalDateTime time;

    public AbstractBanCommand(ICommandProperties commandProperties, IPermission iPermission, ICommandHandler commandHandler) {
        super(commandProperties, iPermission, commandHandler);
        this.commandProperties = commandProperties;
    }

    @Override
    public boolean access(Guild guild, Member member) {
        return member.getRoles().stream().anyMatch(x -> x.hasPermission(Permission.ADMINISTRATOR));
    }

    @Override
    public String logMessage(MessageText message, User user, LocalDateTime time) {
        return String.format("%s was banned for %s hours by %s at %s\n",
                message.convertIntoNameInput(1),
                message.convertIntoParamInput(2),
                user.getName(),
                time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    @Override
    public void timeTillCommand(LocalDateTime localDateTime) {
        time = localDateTime;
    }

    @Override
    public String getCommandNameRaw() {
        return commandProperties.getProperty();
    }

    @Override
    public String description() {
        return "-BanCommand Usage : [Prefix+<ban>] + [Username:Discriminator] + [<Time.InHours> or <Enum.values>] -> 3 param is optional (if already set!)";
    }
}
