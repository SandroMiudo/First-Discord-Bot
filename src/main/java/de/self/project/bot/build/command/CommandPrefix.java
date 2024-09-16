package de.self.project.bot.build.command;

public enum CommandPrefix {
    VERSION_1("!"),VERSION_2("<"),VERSION_3("#");

    private String prefix;

    CommandPrefix(String prefix){
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
