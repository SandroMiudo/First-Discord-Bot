package de.self.project.bot.file;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class FileHandler implements IFileHandler{

    private final File file;

    public FileHandler(File file) {
        this.file = file;
    }

    @Override
    public void writeLog(String logMessage) {
        if(logMessage == null){
            return;
        }
        try {
            FileWriter fileWriter = new FileWriter(file,true);
            fileWriter.write(logMessage);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public File retrieveFile() {
        return file;
    }
}
