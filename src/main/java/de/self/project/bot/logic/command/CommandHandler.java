package de.self.project.bot.logic.command;

import de.self.project.bot.build.command.ICommand;
import net.dv8tion.jda.api.entities.Message;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandHandler implements ICommandHandler{

    @Override
    public boolean compareMessage(Message message, ICommand command) {
        List<String> s = Arrays.stream(message.getContentRaw().trim().split("[ ]")).collect(Collectors.toList());
        // command.getCommandName only gets !ban not the params
        String commandNameRaw = command.getCommandNameRaw();
        if(s.size() >= 1 && s.size() <= 4){
            return s.stream().findFirst().get().equals(commandNameRaw);
        }
        return false;
    }
}
