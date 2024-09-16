package de.self.project.bot.logic.command;

import de.self.project.bot.build.command.ICommand;
import net.dv8tion.jda.api.entities.Message;

public interface ICommandHandler {
    // only compares if the message starts with the command and not if the params are set correctly
    boolean compareMessage(Message message, ICommand command);
}
