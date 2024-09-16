package de.self.project.bot.file;

import net.dv8tion.jda.api.entities.User;

import java.io.File;
import java.time.LocalDateTime;

public interface IFileHandler {
    void writeLog(String logMessage);
    File retrieveFile();
}
