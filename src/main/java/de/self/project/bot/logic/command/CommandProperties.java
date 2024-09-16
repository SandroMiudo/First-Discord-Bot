package de.self.project.bot.logic.command;

import de.self.project.bot.build.command.CommandName;
import de.self.project.bot.build.command.CommandPrefix;
import de.self.project.bot.logic.command.ICommandProperties;

public class CommandProperties implements ICommandProperties {
    String prop;

    @Override
    public void setProperties(CommandPrefix prefix, CommandName name) {
        prop = prefix.getPrefix() + name.name().toLowerCase();
    }

    @Override
    public String getProperty() {
        return prop;
    }
}
