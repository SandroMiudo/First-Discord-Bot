package de.self.project.bot.logic.command;

import de.self.project.bot.build.command.CommandName;
import de.self.project.bot.build.command.CommandPrefix;

public interface ICommandProperties {
    void setProperties(CommandPrefix prefix, CommandName name);
    String getProperty();
}
