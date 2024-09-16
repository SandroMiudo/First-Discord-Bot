package de.self.project.bot.logic.command.impl;

import de.self.project.bot.build.command.Command;
import de.self.project.bot.logic.command.BAN_DURATION;
import de.self.project.bot.logic.command.ICommandHandler;
import de.self.project.bot.logic.command.ICommandProperties;
import de.self.project.bot.logic.command.IPermission;
import de.self.project.bot.util.CommandUtil;
import de.self.project.bot.logic.message.MessageText;
import net.dv8tion.jda.api.entities.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BanCommand extends AbstractBanCommand {

    static class SelfUser{
        private String name;
        private String discriminator;

        public SelfUser(String name, String discriminator) {
            this.name = name;
            this.discriminator = discriminator;
        }
    }

    private Map<SelfUser,List<LocalDateTime>> userBans = new HashMap<>();
    private BAN_DURATION duration;

    public BanCommand(ICommandProperties commandProperties, IPermission iPermission, ICommandHandler commandHandler) {
        super(commandProperties, iPermission, commandHandler);
    }

    @Override
    public boolean validateCommand(MessageText message, List<User> allUserInGuild) {
        String[] splitReg = message.convertIntoNameInput(1).split("[:]");
        SelfUser selfUser = new SelfUser(splitReg[0],splitReg[1]);
        if(hasSize(2,userBans.get(selfUser))){
            return validMessage(message.getContentRaw(),2,allUserInGuild);
        }
        else if(hasSize(3,userBans.get(selfUser))){
            return validMessage(message.getContentRaw(),3,allUserInGuild);
        }
        return false;
    }

    public void ban(String paramTime) {
        retrieveBanInformation(paramTime);
    }

    // if users gets inserted one time to this userBans Map,
    // user ban sets automatic
    // increments times 2 per ban

    @Override
    public void execute(Guild guild, MessageText messageText) {
        // a:b
        String[] name = messageText.convertIntoNameInput(1).split("[:]");
        SelfUser selfUser = new SelfUser(name[0],name[1]);
        userBans.computeIfPresent(selfUser, (x,y) -> {
            if(y.size() == 1){
                y.add(LocalDateTime.now().plusHours(2));
            }
            else{
                y.add(LocalDateTime.now().plusHours((y.size() * 2L)));
            }
            return y;
        });
        userBans.computeIfAbsent(selfUser, x -> List.of(duration.getTime()));
        User user = CommandUtil.getUserFromCommand(name.clone(), guild);
        if(user == null){
            return;
        }
        guild.ban(UserSnowflake.fromId(user.getId()),0).timeout(getLatestTime(selfUser), TimeUnit.SECONDS)
                .complete();
    }

    private long getLatestTime(SelfUser user){
        List<LocalDateTime> lo = userBans.get(user);
        LocalDateTime time = lo.get(lo.size() - 1);
        return time.toEpochSecond(ZoneOffset.UTC);
    }

    private void retrieveBanInformation(String input){
        for(BAN_DURATION b : BAN_DURATION.values()){
            if(b.name().equalsIgnoreCase(input)){
                duration = b;
                return;
            }
        }
        duration = BAN_DURATION.DEFAULT;
        duration.setTime(Integer.parseInt(input));
    }

    private boolean hasSize(int size, List<LocalDateTime> t){
        if(size == 2 && t != null){
            return true;
        }
        if(size == 3 && t == null){
            return true;
        }
        return false;
    }

    private boolean validMessage(String rawMessage, Integer s, List<User> allUsersInGuild){
        String[] split = rawMessage.split("[ ]");
        if(split.length != s){
            return false;
        }
        if(!CommandUtil.userInGuild(split[1],allUsersInGuild)){
            return false;
        }
        if(s == 3){
            // checks if input is Equal to Enum or a valid Integer
            if(!lastInputIsSetCorrectly(split[2])){
                return false;
            }
        }
        return true;
    }

    // if size == 3
    private boolean lastInputIsSetCorrectly(String input){
        for (BAN_DURATION b : BAN_DURATION.values()) {
            if(b.name().equalsIgnoreCase(input)){
                return true;
            }
        }
        int i;
        try{
            i = Integer.parseInt(input);
        }
        catch (IllegalArgumentException ex){
            return false;
        }
        return true;
    }
}
