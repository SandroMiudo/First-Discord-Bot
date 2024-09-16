package de.self.project.bot.logic.message;

import net.dv8tion.jda.api.entities.User;
import org.springframework.data.annotation.Id;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BotMessage{

    @Id
    private Integer id;
    private String message;

    public BotMessage(String message) {
        this.message = message;
    }

    public BotMessage buildMessage(User user) {
        replaceMessageDetails(user.getName());
        return this;
    }

    private void replaceMessageDetails(String username){
        message = Arrays.stream(message.split("[ ]")).reduce("",(x,y) -> {
            if(y.equals("<?>")){
                return x + " "+ username;
            }
            return x + " " + y;
        });
    }

    public static BotMessage retrieveRandomMessage(List<BotMessage> messages){
        Random random = new Random();
        int i = random.nextInt(0, (messages.size() - 1));
        return messages.get(i);
    }

    public String getMessage() {
        return message;
    }
}
