package com.smatech.wlaashal.Model.Objects;

import java.io.Serializable;

public class ChatModel implements Serializable {

    String chatId;
    String lastMessage;
    String seen;
    String name;
    String toId;
    String email;
    String messageSenderId;
    String image;


    public ChatModel(String chatId, String lastMessage, String seen, String name, String toId, String email, String messageSenderId, String image) {
        this.chatId = chatId;
        this.lastMessage = lastMessage;
        this.seen = seen;
        this.name = name;
        this.toId = toId;
        this.email = email;
        this.messageSenderId = messageSenderId;
        this.image = image;
    }

    public String getChatId() {
        return chatId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getSeen() {
        return seen;
    }

    public String getName() {
        return name;
    }

    public String getToId() {
        return toId;
    }

    public String getEmail() {
        return email;
    }

    public String getMessageSenderId() {
        return messageSenderId;
    }

    public String getImage() {
        return image;
    }
}
