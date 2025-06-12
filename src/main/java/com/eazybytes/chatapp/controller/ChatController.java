package com.eazybytes.chatapp.controller;

import com.eazybytes.chatapp.entity.Message;
import com.eazybytes.chatapp.entity.User;
import com.eazybytes.chatapp.model.ChatMessage;
import com.eazybytes.chatapp.repository.MessageRepository;
import com.eazybytes.chatapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;

@Controller
public class ChatController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    // 🌐 WebSocket global message handler
    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public ChatMessage send(ChatMessage chatMessage) {

        User senderUser = userRepository.findByUsername(chatMessage.getSender())
                .orElseThrow(() -> new RuntimeException("Sender user not found"));

        User receiverUser = userRepository.findByUsername(chatMessage.getReceiver())
                .orElseThrow(() -> new RuntimeException("Receiver user not found"));

        Message msgEntity = new Message();
        msgEntity.setContent(chatMessage.getContent());
        msgEntity.setTimestamp(LocalDateTime.now());
        msgEntity.setSender(senderUser);
        msgEntity.setReceiver(receiverUser);
        msgEntity.setRoom(chatMessage.getRoom());

        messageRepository.save(msgEntity);

        chatMessage.setTimestamp(msgEntity.getTimestamp().toString());

        return chatMessage;
    }

    // 💬 WebSocket room-specific message handler
    @MessageMapping("/chat/{room}")
    @SendTo("/topic/{room}")
    public ChatMessage sendToRoom(@DestinationVariable String room, ChatMessage chatMessage) {

        User senderUser = userRepository.findByUsername(chatMessage.getSender())
                .orElseThrow(() -> new RuntimeException("Sender user not found"));

        User receiverUser = userRepository.findByUsername(chatMessage.getReceiver())
                .orElseThrow(() -> new RuntimeException("Receiver user not found"));

        Message msgEntity = new Message();
        msgEntity.setContent(chatMessage.getContent());
        msgEntity.setTimestamp(LocalDateTime.now());
        msgEntity.setSender(senderUser);
        msgEntity.setReceiver(receiverUser);
        msgEntity.setRoom(room);

        messageRepository.save(msgEntity);

        chatMessage.setTimestamp(msgEntity.getTimestamp().toString());
        chatMessage.setRoom(room);

        return chatMessage;
    }

    // 📥 REST endpoint to get all messages of a room
    @GetMapping("/api/messages/{room}")
    @ResponseBody
    public ResponseEntity<List<ChatMessage>> getMessagesByRoom(@PathVariable String room) {
        List<Message> messages = messageRepository.findByRoomOrderByTimestampAsc(room);

        List<ChatMessage> chatMessages = messages.stream().map(message -> {
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setSender(message.getSender().getUsername());
            chatMessage.setReceiver(message.getReceiver().getUsername());
            chatMessage.setContent(message.getContent());
            chatMessage.setRoom(message.getRoom());
            chatMessage.setTimestamp(message.getTimestamp().toString());
            return chatMessage;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(chatMessages);
    }
}
