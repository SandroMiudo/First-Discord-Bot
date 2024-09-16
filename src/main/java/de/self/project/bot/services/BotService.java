package de.self.project.bot.services;

import de.self.project.bot.build.BotConfigs;
import de.self.project.bot.build.BotSettings;
import de.self.project.bot.build.command.ICommand;
import de.self.project.bot.db.ProtocolEntity;
import de.self.project.bot.exception.ConfigurationException;
import de.self.project.bot.logic.DiscordGuild;
import de.self.project.bot.logic.DiscordMember;
import de.self.project.bot.logic.handler.BotConfigurationHandler;
import de.self.project.bot.logic.message.BotMessage;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.utils.AttachmentOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BotService{
    @Value("${spring.member.values}")
    private String values;
    @Value("${spring.guildID}")
    private String guildID;
    @Value("${spring.clientID}")
    private String clientID;

    MessageRepo messageRepo;
    UserRepo userRepo;
    BotMessageRepo botMessageRepo;
    ProtocolRepo protocolRepo;
    MessageRepoV_Bad messageRepoVBad;
    DiscordGuild discordGuild;
    List<ICommand> commands;
    JDA jda;


    public List<ICommand> getCommands() {
        return commands;
    }

    public BotService(MessageRepo messageRepo, UserRepo userRepo, BotMessageRepo botMessageRepo
                    ,ProtocolRepo protocolRepo, MessageRepoV_Bad messageRepoVBad) {
        this.messageRepo = messageRepo;
        this.userRepo = userRepo;
        this.botMessageRepo = botMessageRepo;
        this.protocolRepo = protocolRepo;
        this.messageRepoVBad = messageRepoVBad;
    }

    @PreDestroy
    public void finish(){
        jda.shutdownNow();
    }

    public void initBot(BotSettings botSettings, JDA jda, BotConfigs configs) throws ConfigurationException {
        this.jda = jda;
        botMessageRepo.initMessages(configs.getBotMessages());
        commands = botSettings.getCommands();
        discordGuild = new DiscordGuild(jda.getGuildById(guildID));
        discordGuild.createRoles(botSettings.getAllRoles());
        discordGuild.createCommands(botSettings.getCommands());
        discordGuild.createChannels(botSettings.getChannels());
        discordGuild.addRules(configs.getRules());
        messageRepoVBad.addWords(configs.getBadWords());
        if(botSettings.isMembersPresent()){
            fillDiscordMembers(botSettings, jda);
        }
        checkMembers(jda.getGuildById(guildID));
        runLoop(discordGuild);
    }

    private void fillDiscordMembers(BotSettings botSettings, JDA jda) {
        List<DiscordMember> discordMembers = botSettings.getMembers()
                .map(x -> x.substring(0, x.length() - 2)).map(x -> {
                    String[] s = x.split("[,]");
                    return jda.getUserByTag(s[0], s[1]);
                }).filter(Objects::nonNull).map(DiscordMember::new).collect(Collectors.toList());
        discordMembers.forEach(x -> {
            discordGuild.addMember(x);
            userRepo.saveUser(x);
        });
    }

    // Binds roles to members of guild
    private void checkMembers(Guild g) throws ConfigurationException {
        Map<Member, List<Role>> binding = binding(g);
        DiscordGuild discordGuild = new DiscordGuild(g);
        for(Map.Entry<Member,List<Role>> entry : binding.entrySet()){
              DiscordMember member = new DiscordMember(entry.getKey().getUser());
              member.grantRoles(discordGuild,entry);
        }
    }

    private Map<String,String> mapConfigs (){
        if(values == null || values.isEmpty()){
            return Map.of();
        }
        return Arrays.stream(values.split("[%]")).map(x -> x.split("[;]"))
                .collect(Collectors.toMap(x -> x[0],y -> y[1]));
    }

    private Map<Member,List<Role>> binding(Guild g) throws ConfigurationException {
        BotConfigurationHandler botConfigurationHandler = new BotConfigurationHandler();
        return botConfigurationHandler.configMapping(g, mapConfigs());
    }

    public void timeoutUser(User user,Guild guild) {
        DiscordGuild discordGuild = new DiscordGuild(guild);
        discordGuild.timeout(user);
    }

    public BotMessage getRandomWelcomeMessage(User user) {
        BotMessage b = botMessageRepo.getRandomMessage();
        return b.buildMessage(user);
    }

    private void runLoop(DiscordGuild guild){
        while (true){
            ProtocolEntity protocol = protocolRepo.getProtocol();
            if(protocol != null){
                TextChannel textChannel = guild.retrieveChannel(protocol.getTyp());
                try {
                    textChannel.sendFile(protocol.createFile()).complete();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
