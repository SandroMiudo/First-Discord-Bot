package de.self.project.bot.logic.command;

import java.time.LocalDateTime;

public enum BAN_DURATION {
    PERMA(LocalDateTime.MAX),ONEHOUR(LocalDateTime.now().plusHours(1)),
    ONEDAY(LocalDateTime.now().plusDays(1)),
    ONEWEEK(LocalDateTime.now().plusWeeks(1)),
    ONEMONTH(LocalDateTime.now().plusMonths(1)),
    DEFAULT(LocalDateTime.now()),
    ONEYEAR(LocalDateTime.now().plusYears(1));

    LocalDateTime time;

    BAN_DURATION(LocalDateTime time) {
        this.time = time;
    }


    public void setTime(Integer integer){
        time = LocalDateTime.now().plusHours(integer);
    }

    public LocalDateTime getTime() {
        return time;
    }
}
