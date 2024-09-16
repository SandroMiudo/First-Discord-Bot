package de.self.project.bot.events;

import de.self.project.bot.build.command.ICommand;
import de.self.project.bot.file.IFileHandler;
import de.self.project.bot.logic.Protocol;
import de.self.project.bot.logic.command.GenericCommand;
import de.self.project.bot.logic.command.impl.BanCommand;
import de.self.project.bot.logic.command.impl.BotLogCommand;
import de.self.project.bot.logic.command.impl.HelpCommand;
import de.self.project.bot.logic.command.impl.InsertProtocolCommand;
import de.self.project.bot.logic.message.MessageText;
import de.self.project.bot.services.BotService;
import de.self.project.bot.services.MessageService;
import de.self.project.bot.services.ProtocolService;
import de.self.project.bot.util.BotMessageUtil;
import de.self.project.bot.util.TimeUtil;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.user.UserTypingEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static de.self.project.bot.util.TimeUtil.retrieveTime;

@Component
public class MessageEvents extends ListenerAdapter{

    private final ProtocolService protocolService;
    private final MessageService messageService;
    private BotService botService;
    private List<ICommand> commandList;
    private final IFileHandler fileHandler;


    public MessageEvents(ProtocolService protocolService, MessageService messageService, BotService botService,
                         IFileHandler fileHandler) {
        this.protocolService = protocolService;
        this.messageService = messageService;
        this.botService = botService;
        this.fileHandler = fileHandler;
    }

    private void initCommands(){
        if(commandList == null){
            commandList = botService.getCommands();
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(event.getAuthor().isBot()){
            return;
        }
        initCommands();
        Message message = event.getMessage();
        Member member = event.getMember();
        Guild guild = event.getGuild();
        MessageText messageText = new MessageText(message);
        boolean spam = messageService.checkSpam(Objects.requireNonNull(member).getUser());
        /*if(spam){
            botService.timeoutUser(member.getUser(),event.getGuild());
        }

         */
        Optional<ICommand> command =
                commandList.stream().filter(x -> x.isAllowed(member, message))
                        .filter(x -> checkInstance(x, messageText, mapToUser(guild.getMembers())))
                        .collect(Collectors.toList()).stream().findAny();
        command.ifPresentOrElse(iCommand -> {
            if(!checkValidChannelToCommand(event.getChannel())){
                event.getChannel().sendMessage(BotMessageUtil.denyMessage).complete();
                return;
            }
            instanceCommands(iCommand,messageText,guild,event.getAuthor());
            iCommand.execute(event.getGuild(),messageText);
        },() -> messageService.addMessage(Objects.requireNonNull(message.getMember()).getUser(),message));
    }

    // username = input[1]
    private boolean checkInstance(ICommand command, MessageText message,List<User> users){
        if(command instanceof GenericCommand genericCommand){
            return genericCommand.validateCommand(message,users);
        }
        return false;
    }

    private boolean checkValidChannelToCommand(MessageChannel messageChannel){
        return messageChannel.getName().equals("bot-commands") || messageChannel.getName().equals("private-bot-chat");
    }

    private void instanceCommands(ICommand iCommand, MessageText message, Guild guild, User user){
        String logMessage = iCommand.logMessage(message,user, TimeUtil.buildCurrentTime());
        fileHandler.writeLog(logMessage);
        if(iCommand instanceof BanCommand banCommand){
            String s = message.getContentRaw().split("[ ]")[2];
            banCommand.ban(s);
        }
        else if(iCommand instanceof HelpCommand helpCommand){
            String s = helpCommand.allCommandsSummary(commandList);
            guild.getTextChannelsByName("bot-commands",false).get(0).sendMessage(s).complete();
        }
        else if(iCommand instanceof BotLogCommand botLogCommand){
            try {
                botLogCommand.pushFile(fileHandler.retrieveFile(),guild,message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(iCommand instanceof InsertProtocolCommand protocolCommand){
            if(!message.isFileAttached()) return;
            Protocol protocol = new Protocol(protocolCommand.retrieveTyp(message),message.getAttachments(),
                    retrieveTime(message,2));
            protocolService.addProtocol(protocol);
        }
    }

    private List<User> mapToUser(List<Member> members){
        return members.stream().map(Member::getUser).collect(Collectors.toList());
    }
}
