package com.eazybytes.chatapp.dto;

import com.eazybytes.chatapp.entity.Message;
import java.time.LocalDateTime;

public class ChatMessageDTO {

    private String content;
    private String room;
    private String sender;           // Username of the user
    private LocalDateTime timestamp;

    public ChatMessageDTO() {
    }

    public ChatMessageDTO(Message message) {
        this.content = message.getContent();
        this.room = message.getRoom();
        this.sender = message.getSender().getUsername();  // 👈 Get username from User object
        this.timestamp = message.getTimestamp();
    }

    // Getters and Setters

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
