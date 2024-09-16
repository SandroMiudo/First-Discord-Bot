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

public class DelChannelCommand extends GenericCommand {

    private final ICommandProperties commandProperties;

    public DelChannelCommand(ICommandProperties commandProperties, IPermission iPermission, ICommandHandler commandHandler) {
        super(commandProperties, iPermission, commandHandler);
        this.commandProperties = commandProperties;
    }

    @Override
    public boolean access(Guild guild, Member member) {
        return member.getRoles().stream().anyMatch(x -> x.hasPermission(PermissionUtil.CO_OWNER_PERMISSION()) ||
                x.hasPermission(PermissionUtil.admin));
    }

    @Override
    public void execute(Guild guild, MessageText messageText) {
        GuildChannel guildChannel = guild.getChannels().stream()
                .filter(x -> x.getName().equals(messageText.convertIntoParamInput(1)))
                .findFirst().get();
        guildChannel.delete().complete();
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
        return "-DelChannelCommand Usage : [Prefix+del_ch] + [Channel_Name]";
    }

    @Override
    public String logMessage(MessageText message, User user, LocalDateTime time) {
        return String.format("Channel %s got deleted by %s at %s\n",
                message.convertIntoParamInput(1),
                user.getName(),
                time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    @Override
    public boolean validateCommand(MessageText message, List<User> allUsersInGuild) {
        if(!hasSize(message.getMessageRaw())){
            return false;
        }
        return validChannel(message.convertIntoParamInput(1), message.retrieveGuild());
    }

    private boolean hasSize(String messageRaw){
        return messageRaw.split("[ ]").length == 2;
    }

    private boolean validChannel(String channelInput, Guild guild){
        return guild.getChannels().stream().anyMatch(x -> x.getName().equals(channelInput));
    }
}
