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

public class DeleteCategoryCommand extends GenericCommand {

    private final ICommandProperties commandProperties;

    public DeleteCategoryCommand(ICommandProperties commandProperties, IPermission iPermission, ICommandHandler commandHandler) {
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
        Category category = guild.getCategoriesByName(messageText.convertIntoParamInput(1), false).get(0);
        if(checkInputIsSet(messageText.getMessageRaw())){
            category.getChannels().forEach(x -> x.delete().complete());
            return;
        }
        category.delete().complete();
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
        return "-DeleteCategoryCommand Usage : [Prefix+del_cat] + [Category_Name] + [<?>]\n" +
                "? Params : -wc -> deletes all Channels in this Category\n";
    }

    @Override
    public String logMessage(MessageText message, User user, LocalDateTime time) {
        return String.format("Category %s got deleted by %s at %s\n",
                message.convertIntoParamInput(1),
                user.getName(),
                time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    @Override
    public boolean validateCommand(MessageText message, List<User> allUsersInGuild) {
        if(!hasSize(message.getMessageRaw())){
            return false;
        }
        return checkValidCategory(message.convertIntoParamInput(1), message.retrieveGuild());
    }

    private boolean hasSize(String messageRaw){
        return messageRaw.split("[ ]").length == 2 || messageRaw.split("[ ]").length == 3;
    }

    private boolean checkValidCategory(String input,Guild guild){
        if(input.contains("%")){
            String[] split = input.split("[%]");
            input = split[0] + " " + split[1];
        }
        String temp = input;
        return guild.getCategories().stream().anyMatch(x -> x.getName().equals(temp));
    }

    private boolean checkInputIsSet(String messageRaw){
        String[] split = messageRaw.split("[ ]");
        if(split.length == 3){
            return split[2].equals("wc");
        }
        return false;
    }
}
