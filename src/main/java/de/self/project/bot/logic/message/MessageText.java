package de.self.project.bot.logic.message;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MessageText {

    private Message message;

    public MessageText(Message message) {
        this.message = message;
    }

    public Guild retrieveGuild(){
        return message.getGuild();
    }

    public String getMessageRaw(){
        return message.getContentRaw();
    }

    public String convertIntoNameInput(Integer nameIndex){
        return message.getContentRaw().split("[ ]")[nameIndex];
    }

    public String convertIntoParamInput(Integer index){
        if(index >= message.getContentRaw().split("[ ]").length){
            return null;
        }
        if(message.getContentRaw().split("[ ]")[index].contains("%")){
            String[] split = message.getContentRaw().split("[ ]")[index].split("[%]");
            return split[0] + " " + split[1];
        }
        return message.getContentRaw().split("[ ]")[index];
    }

    public Message analyzeMessage(MessageData messageData){
        if(checkBadWordInMessage(messageData, message.getContentRaw())){
            String messageId = message.getId();
            message.getChannel().deleteMessageById(messageId).complete();
            message.getChannel().sendMessage("Unable to send message! Message contains bad words!").complete();
            return null;
        }
        return message;
    }

    private boolean checkBadWordInMessage(MessageData messageData, String messageRaw){
        String[] split = messageRaw.split("[ ]");
        List<String> badWords = messageData.DB_Data();
        for(int j = 0; j < badWords.size(); j++){
            String badWord = badWords.get(j);
            for(int i = 0; i < split.length; i++){
                String s = split[i];
                if(s.equals(badWord)) {
                    return true;
                }
                for(int k = i+1; k < split.length; k++){
                    s += " " +split[k];
                    if(s.equals(badWord)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private String generateMessage(List<String> words, List<String> badWords){
        String message = "";
        for(String s : words){
            if(badWords.contains(s)){
                message += " **** ";
            }
            else{
                message += s + " ";
            }
        }
        return message.substring(0,message.length()-1);
    }

    public Message.Attachment getAttachments(){
        return message.getAttachments().get(0);
    }

    public boolean isFileAttached(){
        return message.getAttachments().stream().findAny().isPresent();
    }

    public String getContentRaw() {
        return message.getContentRaw();
    }
}
