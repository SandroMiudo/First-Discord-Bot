package de.self.project.bot.logic;

import net.dv8tion.jda.api.entities.Message;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

public class Protocol {

    private ProtocolTyp protocolTyp;
    private Message.Attachment attachment;
    private LocalDateTime time;

    public Protocol(ProtocolTyp protocolTyp, Message.Attachment attachment, LocalDateTime time) {
        this.protocolTyp = protocolTyp;
        this.attachment = attachment;
        this.time = time;
    }

    public String getRawContentOfFileAttach() throws ExecutionException, InterruptedException, IOException {
        return retrieveContent();
    }

    private String retrieveContent() throws ExecutionException, InterruptedException, IOException {
        File file = attachment.downloadToFile().get();
        return Files.readString(file.toPath());
    }

    public String getProtocolTypName() {
        return protocolTyp.name();
    }

    public LocalDateTime getTime() {
        return time;
    }
}

