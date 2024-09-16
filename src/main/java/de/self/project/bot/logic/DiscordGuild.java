package de.self.project.bot.logic;

import de.self.project.bot.build.command.ICommand;
import de.self.project.bot.build.helpers.Channel;
import de.self.project.bot.build.helpers.ChannelTyp;
import de.self.project.bot.logic.message.BotMessage;
import de.self.project.bot.util.PermissionUtil;
import de.self.project.bot.util.TimeUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;

import java.time.Duration;
import java.util.*;

public class DiscordGuild {
    private Guild guild;
    // Token of user
    private String token;
    private List<String> commandsRaw = new ArrayList<>();
    private Map<User,Integer> mapUserTimeOuts = new HashMap<>();

    public DiscordGuild(Guild guild){
        this.guild = guild;
    }

    public void addMember(DiscordMember discordMember){
        guild.addMember(token,discordMember.discordUser())
                .setRoles(guild.getRoles().stream()
                        .filter(x -> x.hasPermission(PermissionUtil.MEMBER_PERMISSION())).toList()).complete();
    }

    // add the permission bit to the role
    public void createRoles(Map<de.self.project.bot.build.helpers.Role, List<Permission>> roles){
        roles.forEach((x,y) -> {
            if(!guild.getRolesByName(x.getRole_Name(),true).isEmpty()){
                return;
            }
            guild.createRole().setPermissions(y)
                    .setColor(new ColorGenerator(new Random()).generateColor()).setName(x.getRole_Name()).complete();
        });
    }

    public User getBot(){
        return guild.getMembers().stream().filter(x -> x.getUser().isBot()).findFirst().get().getUser();
    }

    public void addRolesToMember(DiscordMember discordMember,List<Role> roles){
        Member member = discordMember.retrieveMember(this);
        roles.forEach(x -> guild.addRoleToMember(UserSnowflake.fromId(member.getId()),x).complete());
    }

    // not slash commands
    // something like !help
    public void createCommands(List<ICommand> commands){
        commands.forEach(x -> commandsRaw.add(x.getCommandNameRaw()));
    }

    public Member getMember(DiscordMember discordMember){
        return guild.getMember(discordMember.discordUser());
    }

    public List<net.dv8tion.jda.api.entities.Role> allRoles(){
        return guild.getRoles();
    }

    public void timeout(User user) {
        mapUserTimeOuts.getOrDefault(user,0);
        mapUserTimeOuts.put(user,mapUserTimeOuts.get(user)+1);
        Duration duration = TimeUtil.solveDuration(mapUserTimeOuts.get(user));
        guild.timeoutFor(UserSnowflake.fromId(user.getId()),duration).reason("Spam is not allowed!!!").complete();
    }

    public void createChannels(List<Channel> channels) {
        guild.getChannels().forEach(x -> x.delete().complete());
        guild.getCategories().forEach(x -> x.delete().complete());
        channels.forEach(x -> {
            checkCategory(x);
            Category category = guild.getCategoriesByName(x.getCategory(), true).get(0);
            if(x.getChannelTyp().equals(ChannelTyp.VOICE_CHANNEL)){
                addChannel(guild.createVoiceChannel(x.getChannelName(),category),x);
            }
            else{
                addChannel(guild.createTextChannel(x.getChannelName(),category),x);
            }
        });
    }

    // checks if category is set -> if so nothing has be done otherwise insert category
    private void checkCategory(Channel channel){
        if(guild.getCategories().stream().map(net.dv8tion.jda.api.entities.Channel::getName)
                .noneMatch(x -> x.equalsIgnoreCase(channel.getCategory()))){
            guild.createCategory(channel.getCategory()).complete();
        }
    }

    private void addChannel(ChannelAction<?> channelAction, Channel channel){
        List<Role> memberRoles = retrieveMemberRoles();
        List<Role> roles = new ArrayList<>(retrieveOwnerRoles());
        roles.addAll(retrieveCoOwnerRoles());
        if(!channel.isPublic()){
            channelAction = setPermission(Permission.VIEW_CHANNEL,roles,memberRoles,channelAction);
        }
        if(channel.isMessageSave()){
            channelAction = setPermission(Permission.MESSAGE_SEND,roles,memberRoles,channelAction);
        }
        channelAction.complete();
    }

    private ChannelAction<?> setPermission(Permission permission,List<Role> roles, List<Role> memberRoles, ChannelAction<?> channelAction){
        for (Role role : roles) {
            long id = Objects.requireNonNull(guild.getRoleById(role.getId())).getIdLong();
            channelAction = channelAction.addRolePermissionOverride(id, List.of(permission), null);
        }
        for (Role memberRole : memberRoles) {
            long id = Objects.requireNonNull(guild.getRoleById(memberRole.getId())).getIdLong();
            channelAction = channelAction.addRolePermissionOverride(id, null, List.of(permission));
        }
        return channelAction.addRolePermissionOverride(guild.getPublicRole().getIdLong(),
                null,List.of(permission));
    }


    public void writeIntoGuild(BotMessage botMessage){
        TextChannel textChannel = guild.getTextChannelsByName("welcome", false).get(0);
        textChannel.sendMessage(botMessage.getMessage()).complete();
    }

    public void addRules(List<String> rules){
        String reduce = rules.stream().reduce("", (x, y) -> x + y + "\n");
        guild.getTextChannels().stream().filter(x -> x.getName()
                .equalsIgnoreCase("rules")).findFirst().ifPresent(x -> x.sendMessage(reduce).complete());
    }

    public List<Role> retrieveMemberRoles(){
        return guild.getRoles().stream().filter(x -> x.hasPermission(PermissionUtil.MEMBER_PERMISSION()) &&
                !x.hasPermission(PermissionUtil.CO_OWNER_PERMISSION())).toList();
    }

    public List<Role> retrieveCoOwnerRoles(){
        return guild.getRoles().stream().filter(x -> x.hasPermission(PermissionUtil.CO_OWNER_PERMISSION())).toList();
    }

    public List<Role> retrieveOwnerRoles(){
        return guild.getRoles().stream().filter(x -> x.hasPermission(PermissionUtil.admin)).toList();
    }

    public net.dv8tion.jda.api.entities.TextChannel retrieveChannel(String s){
        return guild.getTextChannels().stream().filter(x -> x.getName().equalsIgnoreCase(s)).findFirst().orElse(null);
    }

}
