package de.self.project.bot.build;

import de.self.project.bot.build.command.Command;
import de.self.project.bot.build.command.CommandName;
import de.self.project.bot.build.command.CommandPrefix;
import de.self.project.bot.build.command.ICommand;
import de.self.project.bot.build.helpers.*;
import de.self.project.bot.events.GuildEvents;
import de.self.project.bot.events.MessageEvents;
import de.self.project.bot.file.FileHandler;
import de.self.project.bot.logic.command.*;
import de.self.project.bot.logic.command.impl.*;
import de.self.project.bot.logic.message.BotMessage;
import de.self.project.bot.services.BotService;
import de.self.project.bot.services.MessageService;
import de.self.project.bot.services.ProtocolService;
import de.self.project.bot.services.UserService;
import de.self.project.bot.util.BadMessageUtil;
import de.self.project.bot.util.BotMessageUtil;
import de.self.project.bot.util.PermissionUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

@Component
public class BotBuilder {

    @Autowired
    BotService botService;
    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;
    @Autowired
    FileHandler fileHandler;
    @Autowired
    ProtocolService protocolService;

    JDA jda;
    String name;
    Map<Role, List<Permission>> allRoles = new HashMap<>();
    List<ICommand> commands = new ArrayList<>();
    Activity activity;
    OnlineStatus onlineStatus;
    Stream<String> members;
    List<Channel> channels;
    List<BotMessage> botMessages;
    List<String> rules;
    List<String> badMessages;

    // Builds a default Bot with the default values
    // Needs a token and the guildID
    public Bot defaultBot(ConfigurationBot configurationBot) throws LoginException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException, InterruptedException {
        return name("BOT").activity(Activity.playing("Computer Science"))
                .status(OnlineStatus.ONLINE)
                .defaultWelcomeMessages()
                .defaultRoles()
                .defaultCommands()
                .defaultChannels()
                .defaultRules()
                .defaultBadMessages()
                .build(configurationBot);
    }

    public BotBuilder defaultRoles(){
        allRoles.put(Role.buildFromRoles(ROLES.OWNER), List.of(PermissionUtil.admin));
        allRoles.put(Role.buildFromRoles(ROLES.CO_OWNER), PermissionUtil.CO_OWNER_PERMISSION());
        allRoles.put(Role.buildFromRoles(ROLES.MEMBER), PermissionUtil.MEMBER_PERMISSION());
        allRoles.put(Role.buildFromRoles(ROLES.NO_ROLE), List.of(Permission.EMPTY_PERMISSIONS));
        return this;
    }

    // prefix [!]
    public BotBuilder defaultCommands() throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        setCommand(CommandName.INVITE,CommandPrefix.VERSION_1,InviteCommand.class);
        setCommand(CommandName.TIMEOUT,CommandPrefix.VERSION_1,TimeOutCommand.class);
        setCommand(CommandName.ROLE,CommandPrefix.VERSION_1,RoleCommand.class);
        setCommand(CommandName.BAN,CommandPrefix.VERSION_1,BanCommand.class);
        setCommand(CommandName.UNBAN,CommandPrefix.VERSION_1,UnbanCommand.class);
        setCommand(CommandName.HELP,CommandPrefix.VERSION_1,HelpCommand.class);
        setCommand(CommandName.ROLE_INS,CommandPrefix.VERSION_1,RoleInsertCommand.class);
        setCommand(CommandName.ROLE_DEL,CommandPrefix.VERSION_1,DelRoleCommand.class);
        setCommand(CommandName.DEL_CH,CommandPrefix.VERSION_1,DelChannelCommand.class);
        setCommand(CommandName.INS_CH,CommandPrefix.VERSION_1,InsertChannelCommand.class);
        setCommand(CommandName.DEL_CAT,CommandPrefix.VERSION_1,DeleteCategoryCommand.class);
        setCommand(CommandName.INS_CAT,CommandPrefix.VERSION_1,InsertCategoryCommand.class);
        setCommand(CommandName.BOT_LOG,CommandPrefix.VERSION_1,BotLogCommand.class);
        setCommand(CommandName.INS_P,CommandPrefix.VERSION_1,InsertProtocolCommand.class);
        return this;
    }

    private void setCommand(CommandName commandName, CommandPrefix commandPrefix,Class<?> commandClass) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        ICommandProperties properties = new CommandProperties();
        properties.setProperties(commandPrefix,commandName);
        IPermission permission = new RolePermission();
        ICommandHandler iCommandHandler = new CommandHandler();
        ICommand command = (ICommand) commandClass.getConstructor(ICommandProperties.class,IPermission.class,ICommandHandler.class)
                .newInstance(properties,permission,iCommandHandler);
        commands.add(command);
    }

    public BotBuilder defaultBadMessages(){
        badMessages = List.of(BadMessageUtil.british,BadMessageUtil.german);
        return this;
    }

    public BotBuilder defaultWelcomeMessages(){
        botMessages = List.of(
                new BotMessage(BotMessageUtil.text1),
                new BotMessage(BotMessageUtil.text2),
                new BotMessage(BotMessageUtil.text3),
                new BotMessage(BotMessageUtil.text4),
                new BotMessage(BotMessageUtil.text5),
                new BotMessage(BotMessageUtil.text6),
                new BotMessage(BotMessageUtil.text7),
                new BotMessage(BotMessageUtil.text8),
                new BotMessage(BotMessageUtil.text9),
                new BotMessage(BotMessageUtil.text10)
        );
        return this;
    }

    public BotBuilder defaultRules(){
        this.rules = List.of(
                BotMessageUtil.rule1,
                BotMessageUtil.rule2,
                BotMessageUtil.rule3,
                BotMessageUtil.rule4,
                BotMessageUtil.rule5,
                BotMessageUtil.rule6,
                BotMessageUtil.rule7,
                BotMessageUtil.rule8
        );
        return this;
    }

    public BotBuilder welcomeMessages(List<BotMessage> botMessages){
        this.botMessages = botMessages;
        return this;
    }

    public BotBuilder commands(List<Command> commands){
        commands.forEach(x -> {
            try {
                setCommand(x.getCommandName(),x.getCommandPrefix(),x.getCommandClass());
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
        return this;
    }

    // can set up members on startup
    // to do that -> put a file in the resources-dir with the name
    public BotBuilder file(boolean file_set) throws IOException {
        if(file_set){
            members = members(Path.of("src/main/resources/handling/startup.txt"));
        }
        return this;
    }

    public BotBuilder customRoles(Map<Role,List<Permission>> permissionMap){
        allRoles.putAll(permissionMap);
        return this;
    }

    public BotBuilder status(OnlineStatus onlineStatus){
        this.onlineStatus = onlineStatus;
        return this;
    }

    public BotBuilder name(String botName){
        this.name = botName;
        return this;
    }

    public BotBuilder activity(Activity activity){
        this.activity = activity;
        return this;
    }

    public BotBuilder defaultChannels(){
        this.channels = retrieveDefaultChannels();
        return this;
    }

    private List<Channel> retrieveDefaultChannels(){
        return List.of(
                new Channel("welcome", ChannelTyp.TEXT_CHANNEL,"WELCOME",true,true),
                new Channel("rules",ChannelTyp.TEXT_CHANNEL,"WELCOME",true,true),
                new Channel("bot-log",ChannelTyp.TEXT_CHANNEL,"ADMIN",true,false),
                new Channel("private-call",ChannelTyp.VOICE_CHANNEL,"ADMIN",false,false),
                new Channel("private-chat",ChannelTyp.TEXT_CHANNEL,"ADMIN",false,false),
                new Channel("private-bot-chat",ChannelTyp.TEXT_CHANNEL,"ADMIN",false,false),
                new Channel("announcements",ChannelTyp.TEXT_CHANNEL,"IMPORTANT",true,true),
                new Channel("protocols",ChannelTyp.TEXT_CHANNEL,"IMPORTANT",true,true),
                new Channel("news", ChannelTyp.NEWS_CHANNEL,"IMPORTANT",true,true),
                new Channel("general",ChannelTyp.TEXT_CHANNEL,"TEXT CHANNEL",false,true),
                new Channel("bot-commands",ChannelTyp.TEXT_CHANNEL,"TEXT CHANNEL",false,true),
                new Channel("Voice 1",ChannelTyp.VOICE_CHANNEL,"VOICE CHANNEL",false,true),
                new Channel("Voice 2",ChannelTyp.VOICE_CHANNEL,"VOICE CHANNEL",false,true),
                new Channel("Voice 3",ChannelTyp.VOICE_CHANNEL,"VOICE CHANNEL",false,true));
    }

    // custom Channels + Default
    public BotBuilder setChannels(List<Channel> channels){
        this.channels = new ArrayList<>(channels);
        this.channels.addAll(retrieveDefaultChannels());
        return this;
    }

    public Bot build(ConfigurationBot configurationBot) throws LoginException, InterruptedException {
        createJDA(configurationBot);
        BotSettings botSettings = new BotSettings(allRoles,configurationBot.getGuildID(),commands,channels);
        if(members != null){
            botSettings = new BotSettings(members,allRoles,configurationBot.getGuildID(),commands,channels);
        }
        BotConfigs botConfigs = new BotConfigs(badMessages,rules,botMessages);
        return new Bot(botService,botSettings,jda,botConfigs);
    }

    private void createJDA(ConfigurationBot configurationBot) throws LoginException, InterruptedException {
        jda = JDABuilder.create(configurationBot.getToken(),List.of(GatewayIntent.GUILD_MEMBERS,GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_INVITES,GatewayIntent.GUILD_BANS,
                        GatewayIntent.GUILD_EMOJIS))
                .setStatus(OnlineStatus.ONLINE).setActivity(activity)
                .addEventListeners(new GuildEvents(botService,userService)
                        ,new MessageEvents(protocolService,messageService,botService,fileHandler))
                .build().awaitReady();
    }

    // name,discriminator:role1,role2
    private Stream<String> members(Path path) throws IOException {
        String file_AsString = Files.readString(path).trim();
        String[] splitAtRegEx = file_AsString.split("[\n]");
        return Arrays.stream(splitAtRegEx).filter(x -> !x.startsWith("//") && !x.isEmpty())
                .filter(x -> x.split("[,]").length == 2 && (x.endsWith(":") || x.split("[:]").length == 2));
    }
}
