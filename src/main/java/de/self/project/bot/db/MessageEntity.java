package de.self.project.bot.db;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public class MessageEntity {
    @Id
    private Long id;
    private String content;
    private String discriminator;
    private String name;
    private LocalDateTime time;


    public MessageEntity(String content, String name, String discriminator, LocalDateTime time) {
        this.content = content;
        this.discriminator = discriminator;
        this.name = name;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getTime() {
        return time;
    }
}
