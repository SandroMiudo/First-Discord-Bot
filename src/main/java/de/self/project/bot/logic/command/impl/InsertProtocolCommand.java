package de.self.project.bot.logic.command.impl;

import de.self.project.bot.logic.ProtocolTyp;
import de.self.project.bot.logic.command.GenericCommand;
import de.self.project.bot.logic.command.ICommandHandler;
import de.self.project.bot.logic.command.ICommandProperties;
import de.self.project.bot.logic.command.IPermission;
import de.self.project.bot.logic.message.MessageText;
import de.self.project.bot.util.PermissionUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static de.self.project.bot.util.TimeUtil.correctTimeInput;

public class InsertProtocolCommand extends GenericCommand {

    private final ICommandProperties commandProperties;

    public InsertProtocolCommand(ICommandProperties commandProperties, IPermission iPermission, ICommandHandler commandHandler) {
        super(commandProperties, iPermission, commandHandler);
        this.commandProperties = commandProperties;
    }

    @Override
    public boolean access(Guild guild, Member member) {
        return member.getRoles().stream().anyMatch(x -> x.hasPermission(PermissionUtil.admin) ||
                x.hasPermission(PermissionUtil.CO_OWNER_PERMISSION()));
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
        return "-InsertProtocolCommand Usage : [Prefix+ins_p] + [<?>] ? = protocol,news,announce + [Time(long value)]\n" +
                "Insert a given file to db and put it to the channel when time is okay.";
    }

    @Override
    public String logMessage(MessageText message, User user, LocalDateTime time) {
        return String.format("A Protocol got inserted by %s at %s\n",
                user.getName(),
                time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    @Override
    public boolean validateCommand(MessageText message, List<User> allUsersInGuild) {
        if(!hasSize(message.getMessageRaw())){
            return false;
        }
        return correctProtocolTyp(message.convertIntoParamInput(1)) && correctTimeInput(message.convertIntoParamInput(2));
    }

    private boolean hasSize(String rawMessage){
        return rawMessage.split("[ ]").length == 3;
    }

    private boolean correctProtocolTyp(String protocolTypInput){
        return ProtocolTyp.PROTOCOLS.name().toLowerCase().contains(protocolTypInput) ||
                ProtocolTyp.NEWS.name().toLowerCase().contains(protocolTypInput) ||
                ProtocolTyp.ANNOUNCEMENTS.name().toLowerCase().contains(protocolTypInput);
    }

    // 202211020000

    public ProtocolTyp retrieveTyp(MessageText message){
        String s = message.convertIntoParamInput(1);
        return switch (s) {
            case "protocol" -> ProtocolTyp.PROTOCOLS;
            case "news" -> ProtocolTyp.NEWS;
            case "announce" -> ProtocolTyp.ANNOUNCEMENTS;
            default -> null;
        };
    }
}
