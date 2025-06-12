package com.eazybytes.chatapp.controller;

import com.eazybytes.chatapp.entity.Message;
import com.eazybytes.chatapp.entity.User;
import com.eazybytes.chatapp.model.ChatMessage;
import com.eazybytes.chatapp.repository.MessageRepository;
import com.eazybytes.chatapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat-messages") // ✅ Changed from /api/messages to /api/chat-messages
public class MessageRestController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    // ✅ Get all messages of a specific room (ordered by timestamp ASC)
    @GetMapping("/room/{room}") // ✅ Changed path to make it distinct
    public List<ChatMessage> getMessagesByRoom(@PathVariable String room) {
        List<Message> messages = messageRepository.findByRoomOrderByTimestampAsc(room);

        return messages.stream().map(msg -> {
            ChatMessage cm = new ChatMessage();
            cm.setSender(msg.getSender().getUsername());
            cm.setReceiver(msg.getReceiver().getUsername());
            cm.setContent(msg.getContent());
            cm.setTimestamp(msg.getTimestamp().toString());
            cm.setRoom(msg.getRoom());
            return cm;
        }).collect(Collectors.toList());
    }

    // ✅ Optional: Get private messages between two users (ordered by timestamp ASC)
    @GetMapping("/between")
    public List<ChatMessage> getMessagesBetweenUsers(@RequestParam String sender,
                                                     @RequestParam String receiver) {

        Optional<User> senderUser = userRepository.findByUsername(sender);
        Optional<User> receiverUser = userRepository.findByUsername(receiver);

        if (senderUser.isPresent() && receiverUser.isPresent()) {
            List<Message> messages = messageRepository
                    .findBySenderAndReceiverOrderByTimestampAsc(senderUser.get(), receiverUser.get());

            return messages.stream().map(msg -> {
                ChatMessage cm = new ChatMessage();
                cm.setSender(msg.getSender().getUsername());
                cm.setReceiver(msg.getReceiver().getUsername());
                cm.setContent(msg.getContent());
                cm.setTimestamp(msg.getTimestamp().toString());
                cm.setRoom(msg.getRoom());
                return cm;
            }).collect(Collectors.toList());
        }

        return List.of(); // Empty list if sender or receiver not found
    }

    // ✅ DELETE endpoint to clear messages of a specific room
    @DeleteMapping("/room/{room}")
    @Transactional
    public void deleteMessagesByRoom(@PathVariable String room) {
        messageRepository.deleteByRoom(room);
    }
}
