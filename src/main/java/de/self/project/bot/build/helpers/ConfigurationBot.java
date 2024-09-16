package de.self.project.bot.build.helpers;

public class ConfigurationBot {

    private String token;
    private String guildID;

    public ConfigurationBot(String token, String guildID) {
        validConfigInput(token,guildID);
        this.token = token;
        this.guildID = guildID;
    }

    private void validConfigInput(String token, String guildID){

    }

    public String getToken() {
        return token;
    }

    public String getGuildID() {
        return guildID;
    }
}
