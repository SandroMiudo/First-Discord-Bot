package de.self.project.bot.logic.command;

import de.self.project.bot.build.command.ICommand;
import net.dv8tion.jda.api.entities.Member;

public class RolePermission implements IPermission{
    @Override
    public boolean hasPermission(Member member, ICommand command) {
        return command.access(member.getGuild(), member);
    }
}
