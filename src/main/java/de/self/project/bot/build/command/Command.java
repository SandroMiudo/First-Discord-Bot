package de.self.project.bot.build.command;

public class Command {
    private CommandName commandName;
    private CommandPrefix commandPrefix;
    private Class<?> commandClass;

    public Command(CommandName commandName, CommandPrefix commandPrefix,Class<?> commandClass) {
        this.commandName = commandName;
        this.commandPrefix = commandPrefix;
        this.commandClass = commandClass;
    }

    public String getCommandNameRaw(){
        return commandName.name();
    }

    public String getCommandPrefixRaw(){
        return commandPrefix.name();
    }

    public CommandName getCommandName() {
        return commandName;
    }

    public CommandPrefix getCommandPrefix() {
        return commandPrefix;
    }


    public Class<?> getCommandClass() {
        return commandClass;
    }
}
