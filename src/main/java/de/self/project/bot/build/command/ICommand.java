package de.self.project.bot.build.command;

import de.self.project.bot.logic.message.MessageText;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.time.LocalDateTime;

public interface ICommand{
    boolean access(Guild guild, Member member);
    void execute(Guild guild, MessageText messageText);
    void timeTillCommand(LocalDateTime localDateTime);
    String getCommandNameRaw();
    String description();
    boolean isAllowed(Member member, Message message);
    String logMessage(MessageText message, User user,LocalDateTime time);
}
