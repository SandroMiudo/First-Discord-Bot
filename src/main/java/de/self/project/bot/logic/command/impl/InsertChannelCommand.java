package de.self.project.bot.logic.command.impl;

import de.self.project.bot.logic.command.GenericCommand;
import de.self.project.bot.logic.command.ICommandHandler;
import de.self.project.bot.logic.command.ICommandProperties;
import de.self.project.bot.logic.command.IPermission;
import de.self.project.bot.logic.message.MessageText;
import de.self.project.bot.util.PermissionUtil;
import net.dv8tion.jda.api.entities.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class InsertChannelCommand extends GenericCommand {
    private final ICommandProperties commandProperties;

    public InsertChannelCommand(ICommandProperties commandProperties, IPermission iPermission, ICommandHandler commandHandler) {
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
        setChannel(messageText.convertIntoParamInput(2),guild,messageText.convertIntoParamInput(1),
                    messageText.convertIntoParamInput(3));
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
        return "-InsertChannelCommand Usage : [Prefix+<ins_ch>] + [Channel_Name] + [<?>]\n" +
                "? Options : -v -t + [Category_Name]";
    }

    @Override
    public String logMessage(MessageText message, User user, LocalDateTime time) {
        return String.format("Channel %s got inserted into the guild by %s at %s\n",
                message.convertIntoParamInput(1),
                user.getName(),
                time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    @Override
    public boolean validateCommand(MessageText message, List<User> allUsersInGuild) {
        if(!hasSize(message.getMessageRaw())){
            return false;
        }
        if(!(validChannel(message.retrieveGuild(),message.convertIntoParamInput(1)) &&
                validCategory(message.retrieveGuild(),message.convertIntoParamInput(3)))){
            return false;
        }
        return validChannelDeciderInput(message.convertIntoParamInput(2));
    }

    private boolean hasSize(String messageRaw){
        return messageRaw.split("[ ]").length == 4;
    }

    // not already in guild
    private boolean validChannel(Guild guild, String channelInput){
        return guild.getChannels().stream().noneMatch(x -> x.getName().equals(channelInput)) && !channelInput.isEmpty();
    }

    private boolean validChannelDeciderInput(String channelDeciderInput){
        return channelDeciderInput.equals("t") || channelDeciderInput.equals("v");
    }

    private boolean validCategory(Guild guild, String categoryInput){
        if(categoryInput.contains("%")){
            String[] split = categoryInput.split("[%]");
            categoryInput = split[0] + " " + split[1];
        }
        String finalTemp = categoryInput;
        return guild.getChannels().stream().anyMatch(x -> x.getName().equals(finalTemp));
    }

    private void setChannel(String channelDeciderInput, Guild guild, String channelName, String categoryName){
        // v = voice and t = text
        Category category = guild.getCategories().stream().filter(x -> x.getName().equals(categoryName)).findFirst().get();
        if(channelDeciderInput.equals("v")){
            guild.createVoiceChannel(channelName,category).complete();
        }
        else if(channelDeciderInput.equals("t")){
            guild.createTextChannel(channelName,category).complete();
        }
    }
}
