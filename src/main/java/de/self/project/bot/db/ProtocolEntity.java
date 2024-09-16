package de.self.project.bot.db;

import org.springframework.data.annotation.Id;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class ProtocolEntity {
    @Id
    Long id;
    LocalDateTime time;
    String typ;
    String content;

    public ProtocolEntity(LocalDateTime time, String typ, String content) {
        this.time = time;
        this.typ = typ;
        this.content = content;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getTyp() {
        return typ;
    }

    public String getContent() {
        return content;
    }

    public File createFile() throws IOException {
        File file = new File("src/main/resources/handling/protocol.txt");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(content);
        fileWriter.close();
        return file;
    }

    @Override
    public String toString() {
        return "ProtocolEntity{" +
                "time=" + time +
                ", typ='" + typ + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
