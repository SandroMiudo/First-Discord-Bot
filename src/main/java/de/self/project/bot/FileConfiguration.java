package de.self.project.bot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;

@Configuration
public class FileConfiguration {

    @Bean
    String retrievePath(){
        return Path.of("src/main/resources/handling/Bot-log.txt").toString();
    }
}
