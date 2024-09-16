package de.self.project.bot.logic.command.impl;

import de.self.project.bot.logic.command.GenericCommand;
import de.self.project.bot.logic.command.ICommandHandler;
import de.self.project.bot.logic.command.ICommandProperties;
import de.self.project.bot.logic.command.IPermission;
import de.self.project.bot.util.CommandUtil;
import de.self.project.bot.logic.message.MessageText;
import de.self.project.bot.util.PermissionUtil;
import net.dv8tion.jda.api.entities.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

// something like !unban user
public class UnbanCommand extends GenericCommand {

    private final ICommandProperties commandProperties;

    public UnbanCommand(ICommandProperties commandProperties, IPermission iPermission, ICommandHandler commandHandler) {
        super(commandProperties, iPermission, commandHandler);
        this.commandProperties = commandProperties;
    }


    @Override
    public boolean access(Guild guild, Member member) {
        return member.getRoles().stream().anyMatch(x -> x.hasPermission(PermissionUtil.admin) ||
                x.hasPermission(PermissionUtil.CO_OWNER_PERMISSION()));
    }

    @Override
    public void execute(Guild guild, MessageText messageText) {
        User user = CommandUtil.retrieveBannedUser(messageText.convertIntoNameInput(1).split("[:]"), guild);
        if(user == null){
            return;
        }
        guild.unban(UserSnowflake.fromId(user.getId())).complete();
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
        return "-UnbanCommand Usage : [Prefix+<unban>] + [Username:Discriminator]";
    }

    @Override
    public String logMessage(MessageText message, User user, LocalDateTime time) {
        return message.convertIntoNameInput(1) + " was unbanned by " + user.getName() +
                "at " + time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) +"\n";
    }

    @Override
    public boolean validateCommand(MessageText message, List<User> allUsersInGuild) {
        return hasSize(2, message.getContentRaw());
    }

    private boolean hasSize(int size, String messageRaw){
        return messageRaw.split("[ ]").length == size;
    }
}
