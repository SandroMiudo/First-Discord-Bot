package de.self.project.bot.logic.command.impl;

import de.self.project.bot.build.command.ICommand;
import de.self.project.bot.build.helpers.ROLES;
import de.self.project.bot.logic.command.GenericCommand;
import de.self.project.bot.logic.command.ICommandHandler;
import de.self.project.bot.logic.command.ICommandProperties;
import de.self.project.bot.logic.command.IPermission;
import de.self.project.bot.logic.message.MessageText;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class HelpCommand extends GenericCommand {

    private final ICommandProperties commandProperties;

    public HelpCommand(ICommandProperties commandProperties, IPermission iPermission, ICommandHandler commandHandler) {
        super(commandProperties, iPermission, commandHandler);
        this.commandProperties = commandProperties;
    }

    @Override
    public boolean access(Guild guild, Member member) {
        return true;
    }

    @Override
    public void execute(Guild guild, MessageText messageText) {}

    @Override
    public void timeTillCommand(LocalDateTime localDateTime) {

    }

    @Override
    public String getCommandNameRaw() {
        return commandProperties.getProperty();
    }

    @Override
    public String description() {
        return "-HelpCommand Usage : [Prefix+<help>]";
    }

    @Override
    public String logMessage(MessageText message, User user, LocalDateTime time) {
        return null;
    }

    @Override
    public boolean validateCommand(MessageText message, List<User> allUsersInGuild) {
        return message.getContentRaw().split("[ ]").length == 1;
    }

    public String allCommandsSummary(List<ICommand> iCommands){
        return iCommands.stream().reduce("", (x, y) -> x + y.description() + "\n", (x, y) -> x + y);
    }
}
