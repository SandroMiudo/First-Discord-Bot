package de.self.project.bot.events;

import de.self.project.bot.logic.DiscordGuild;
import de.self.project.bot.logic.DiscordMember;
import de.self.project.bot.logic.message.BotMessage;
import de.self.project.bot.services.BotService;
import de.self.project.bot.services.UserService;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GuildEvents extends ListenerAdapter implements EventListener {

    private BotService botService;
    private UserService userService;

    public GuildEvents(BotService botService, UserService userService) {
        this.botService = botService;
        this.userService = userService;
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        BotMessage message = botService.getRandomWelcomeMessage(event.getUser());
        userService.save(event.getUser());
        DiscordGuild discordGuild = new DiscordGuild(event.getGuild());
        DiscordMember discordMember = new DiscordMember(event.getUser());
        discordGuild.addRolesToMember(discordMember,discordGuild.retrieveMemberRoles());
        discordGuild.writeIntoGuild(message);
    }

    public void doSome(){
        List<String> sList = List.of("a:b","c:d","e:f","g:h");
        ArrayList<String> s = sList.stream().collect(ArrayList::new, (x, y) -> {
            String[] split = y.split("[:]");
            x.add(split[0]);
            x.add(split[1]);
        }, ArrayList::addAll);

    }
}
