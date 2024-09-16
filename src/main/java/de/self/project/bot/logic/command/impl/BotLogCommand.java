package de.self.project.bot.logic.command.impl;

import de.self.project.bot.logic.command.GenericCommand;
import de.self.project.bot.logic.command.ICommandHandler;
import de.self.project.bot.logic.command.ICommandProperties;
import de.self.project.bot.logic.command.IPermission;
import de.self.project.bot.logic.message.MessageText;
import de.self.project.bot.util.PermissionUtil;
import de.self.project.bot.util.TimeUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

public class BotLogCommand extends GenericCommand {

    private final ICommandProperties commandProperties;

    public BotLogCommand(ICommandProperties commandProperties, IPermission iPermission, ICommandHandler commandHandler) {
        super(commandProperties, iPermission, commandHandler);
        this.commandProperties = commandProperties;
    }

    @Override
    public boolean access(Guild guild, Member member) {
        return member.getRoles().stream().anyMatch(x -> x.hasPermission(PermissionUtil.admin)
                    || x.hasPermission(PermissionUtil.CO_OWNER_PERMISSION()));
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
        return "-BotLogCommand Usage : [Prefix+bot_log] + [Time(long)] \n" +
                "Time has to be set in ISO-LOCAL-DATE\n" +
                "Example : 2012-12-01 / year-month-day\n";
    }

    @Override
    public String logMessage(MessageText message, User user, LocalDateTime time) {
        return String.format("%s command to see bot log at %s\n",
                user.getName(),
                time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    @Override
    public boolean validateCommand(MessageText message, List<User> allUsersInGuild) {
        return hasSize(message.getMessageRaw()) && TimeUtil.correctTimeInput(message.convertIntoParamInput(1));
    }

    private boolean hasSize(String messageRaw){
        return messageRaw.split("[ ]").length == 2;
    }

    public void pushFile(File file, Guild guild, MessageText messageText) throws IOException {
        // bot-log can't be null if not deleted
        TextChannel textChannel = guild.getTextChannelsByName("bot-log", false).get(0);
        Objects.requireNonNull(guild.getTextChannelById(textChannel.getId()))
                .sendFile(Objects.requireNonNull(readFile(file, messageText)))
                .complete();
    }

    private File readFile(File file,MessageText message) throws IOException {
        LocalDateTime timeToCompare = TimeUtil.retrieveTime(message,1);
        final int[] counter = {0};
        Scanner scanner = new Scanner(file);
        File fileToWrite = new File("src/main/resources/handling/MinimalBot-Log.txt");
        FileWriter fileWriter = new FileWriter(fileToWrite);
        while (scanner.hasNextLine()){
            if(counter[0] >= 50){
                break;
            }
            String s = scanner.nextLine();
            Optional<LocalDateTime> optionalTime = getDateFromString(s);
            if(optionalTime.isPresent() && optionalTime.get().isAfter(timeToCompare)){
                counter[0]++;
                fileWriter.write(s+"\n");
            }
        }
        fileWriter.close();
        return fileToWrite;
    }

    private Optional<LocalDateTime> getDateFromString(String s){
        String[] string = s.split("[ ]");
        for(String si : string){
            try{
                if(si.contains("\n")) si = si.substring(0,si.length()-1);
                LocalDateTime parse = LocalDateTime.parse(si);
                return Optional.of(parse);
            }
            catch (DateTimeParseException ignored){}
        }
        return Optional.empty();
    }
}
