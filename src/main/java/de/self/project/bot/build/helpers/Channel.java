package de.self.project.bot.build.helpers;

import org.springframework.lang.NonNull;

public class Channel {
    private String channelName;
    private ChannelTyp channelTyp;
    private String category;
    private boolean messageSave;
    private boolean isPublic;

    public Channel(@NonNull String channelName, ChannelTyp channelTyp, @NonNull String category, boolean messageSave,
                   boolean isPublic){
        this.channelName = channelName;
        this.channelTyp = channelTyp;
        this.category = category;
        this.messageSave = messageSave;
        this.isPublic = isPublic;
    }

    public String getChannelName() {
        return channelName;
    }

    public ChannelTyp getChannelTyp() {
        return channelTyp;
    }

    public String getCategory() {
        return category;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public boolean isMessageSave() {
        return messageSave;
    }
}
