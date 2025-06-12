package com.eazybytes.chatapp.model;

import jakarta.persistence.*;  // JPA imports (agar tum Hibernate ke sath ho to ye theek hai)

@Entity
@Table(name = "chat_messages") // Table ka naam bhi specify kar rahe hain
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Primary key ke liye id add kiya

    private String sender;
    private String content;
    private String timestamp;
    private String receiver;

    // Naya field add kiya: room
    private String room;

    // Getters and setters

    public Long getId() {
        return id;
    }

    // id setter optional hota hai, agar chaho to add kar sakti ho

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    
    public String getReceiver() { return receiver; }
    public void setReceiver(String receiver) { this.receiver = receiver; }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }
}
