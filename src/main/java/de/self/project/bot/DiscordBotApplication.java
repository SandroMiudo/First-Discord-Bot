package de.self.project.bot;

import de.self.project.bot.build.Bot;
import de.self.project.bot.build.BotBuilder;
import de.self.project.bot.build.helpers.ConfigurationBot;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@SpringBootApplication
public class DiscordBotApplication{
    @Value("${spring.token}")
    String token;
    @Value("${spring.guildID}")
    String guild;

    public static void main(String[] args) {
        SpringApplication.run(DiscordBotApplication.class, args);
    }

    @Bean
    File file(String config){
        return new File(config);
    }

    @Bean
    CommandLineRunner commandLineRunner(BotBuilder botBuilder){
        return x -> {
            ConfigurationBot configurationBot = new ConfigurationBot(token,guild);
            Bot bot = botBuilder.defaultBot(configurationBot);
            bot.init();
        };
    }
}
