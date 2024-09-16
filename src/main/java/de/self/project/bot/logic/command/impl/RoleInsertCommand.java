package de.self.project.bot.logic.command.impl;

import de.self.project.bot.logic.command.GenericCommand;
import de.self.project.bot.logic.command.ICommandHandler;
import de.self.project.bot.logic.command.ICommandProperties;
import de.self.project.bot.logic.command.IPermission;
import de.self.project.bot.logic.message.MessageText;
import de.self.project.bot.util.CommandUtil;
import de.self.project.bot.util.PermissionUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RoleInsertCommand extends GenericCommand{

    final ICommandProperties commandProperties;

    public RoleInsertCommand(ICommandProperties commandProperties, IPermission iPermission, ICommandHandler commandHandler) {
        super(commandProperties, iPermission, commandHandler);
        this.commandProperties = commandProperties;
    }


    @Override
    public boolean access(Guild guild, Member member) {
        return member.getRoles().stream().anyMatch(x -> x.hasPermission(PermissionUtil.admin));
    }

    @Override
    public void execute(Guild guild, MessageText messageText) {
        String param1 = messageText.convertIntoParamInput(2);
        String param2 = messageText.convertIntoParamInput(3);
        List<Permission> permission = switch (param1) {
            case "owner" -> List.of(PermissionUtil.admin);
            case "co" -> PermissionUtil.CO_OWNER_PERMISSION();
            case "member" -> PermissionUtil.MEMBER_PERMISSION();
            default -> new ArrayList<>();
        };
        Random random = new Random();
        List<Integer> ints = random.ints(3, 0, 255).boxed().toList();
        Color color = switch (param2){
            case "random" -> new Color(ints.get(0),ints.get(1),ints.get(2));
            default -> Color.getColor(param2);
        };
        guild.createRole().setPermissions(permission).setColor(color)
                .setName(messageText.getMessageRaw().split("[ ]")[1]).complete();
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
        return "-RoleInsertCommand Usage : [Prefix+<role_ins>] + [Role_Name] + [<?>] + [!]\n" +
                "? -> owner : ? -> co (co_owner) : ? -> member ::: ? -> permission rights\n " +
                "! -> random : ! -> (color)\n";
    }

    @Override
    public String logMessage(MessageText message, User user, LocalDateTime time) {
        return String.format("role %s with permission %s got inserted by %s at %s\n",
                message.convertIntoNameInput(1),
                message.convertIntoParamInput(2),
                user.getName(),
                time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    @Override
    public boolean validateCommand(MessageText message, List<User> allUsersInGuild) {
        if(!checkIfCorrectSize(message.getContentRaw())) return false;
        if(!role_Name_NonEmpty(message.getContentRaw())) return false;
        boolean b =  correctColorSetting(message.getContentRaw());
        boolean c = correctPermission(message.getContentRaw());
        return b & c;
    }

    private boolean checkIfCorrectSize(String messageRaw){
        return messageRaw.split("[ ]").length == 4;
    }

    private boolean role_Name_NonEmpty(String messageRaw){
        return !messageRaw.split("[ ]")[1].isEmpty();
    }

    private boolean correctPermission(String messageRaw){
        String[] split = messageRaw.split("[ ]");
        return split[2].equals("owner") || split[2].equals("co") || split[2].equals("member");
    }

    private boolean correctColorSetting(String messageRaw){
        String[] split = messageRaw.split("[ ]");
        Color color = Color.getColor(split[3]);
        if(color != null) return true;
        return split[3].equals("random");
    }
}
