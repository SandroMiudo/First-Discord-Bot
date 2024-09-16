package de.self.project.bot.db;

import org.springframework.data.annotation.Id;

public class Member {
    @Id
    private Long id;
    private String name;
    private String discriminator;

    public Member(String name, String discriminator) {
        this.name = name;
        this.discriminator = discriminator;
    }

    public String getName() {
        return name;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public Long getId() {
        return id;
    }
}
