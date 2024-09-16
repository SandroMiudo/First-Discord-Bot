package de.self.project.bot.db;

import org.springframework.data.annotation.Id;

public class MessageEntityBadWord {
    @Id
    private Long id;
    private String message;

    public MessageEntityBadWord(Long id, String message) {
        this.id = id;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}
