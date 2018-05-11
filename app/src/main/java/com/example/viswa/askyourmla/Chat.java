package com.example.viswa.askyourmla;

import java.util.Date;

/**
 * Created by viswa on 13-04-2018.
 */

public class Chat
{
    private String MessageText;
    private String MessageUser;
    private long MessageTime;

    public Chat(String messageText, String messageUser) {
        this.MessageText = messageText;
        this.MessageUser = messageUser;

        MessageTime=new Date().getTime();
    }

    public Chat() {

    }

    public String getMessageText() {
        return MessageText;
    }

    public void setMessageText(String messageText) {
        MessageText = messageText;
    }

    public String getMessageUser() {
        return MessageUser;
    }

    public void setMessageUser(String messageUser) {
        MessageUser = messageUser;
    }

    public long getMessageTime() {
        return MessageTime;
    }

    public void setMessageTime(long messageTime) {
        MessageTime = messageTime;
    }
}
