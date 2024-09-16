package de.self.project.bot.util;

import de.self.project.bot.logic.message.MessageText;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TimeUtil {

    // multipliers to extend timeout
    private static Integer LOW_MULTIPLIER = 2;
    private static Integer MID_MULTIPLIER = 5;
    private static Integer HIGH_MULTIPLIER = 10;
    private static Integer EXTREM_MULTIPLIER = 20;

    private static final Integer TIME_VALUE = 12;
    private static final Integer HOUR_FIX = 24;
    private static final Integer MINUTE_FIX = 60;
    private static final Integer DAY_FIX = 31;
    private static final Integer MONTH_FIX = 12;
    private static final Integer YEAR_FIX = 9999;
    private static final Integer MIN_TIME_VALUE = 0;

    public static Duration solveDuration(Integer i){
        if(i == 1){
            return Duration.ofMinutes(15);
        }
        else if(i < 5){
            return Duration.ofMinutes((long) LOW_MULTIPLIER * i * 15);
        }
        else if(i < 10){
            return Duration.ofMinutes((long) MID_MULTIPLIER * i * 15);
        }
        else if(i < 20){
            return Duration.ofMinutes((long) HIGH_MULTIPLIER * i * 15);
        }
        return Duration.ofMinutes((long) EXTREM_MULTIPLIER * i * 15);
    }

    public static LocalDateTime retrieveTime(MessageText message,Integer i){
        String s = message.convertIntoParamInput(i);
        int year = Integer.parseInt(s.substring(0,4));
        int month = Integer.parseInt(s.substring(4,6));
        int day = Integer.parseInt(s.substring(6,8));
        int hour = Integer.parseInt(s.substring(8,10));
        int minute = Integer.parseInt(s.substring(10,12));
        return LocalDateTime.of(year,month,day,hour,minute);
    }

    public static boolean correctTimeInput(String timeInput){
        if(timeInput.length() != TIME_VALUE){
            return false;
        }
        try{
            Long.parseLong(timeInput);
        }catch (NumberFormatException e){
            return false;
        }
        long year = Long.parseLong(timeInput.substring(0, 3));
        long month = Long.parseLong(timeInput.substring(4,5));
        long day = Long.parseLong(timeInput.substring(6,7));
        long hour = Long.parseLong(timeInput.substring(8,9));
        long minute = Long.parseLong(timeInput.substring(10,11));
        return controlTime(year,YEAR_FIX) && controlTime(month,MONTH_FIX) && controlTime(day,DAY_FIX)
                && controlTime(hour,HOUR_FIX) && controlTime(minute,MINUTE_FIX);
    }

    private static boolean controlTime(Long l,Integer controlValue){
        return l.intValue() <= controlValue && l.intValue() >= MIN_TIME_VALUE;
    }

    public static LocalDateTime buildCurrentTime(){
        LocalDateTime localDateTime = LocalDateTime.now();
        return LocalDateTime.of(localDateTime.getYear(),localDateTime.getMonthValue(),
                localDateTime.getDayOfMonth(),localDateTime.getHour(),localDateTime.getMinute(),localDateTime.getSecond());
    }
}
