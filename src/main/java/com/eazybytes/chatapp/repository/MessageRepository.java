package com.eazybytes.chatapp.repository;

import com.eazybytes.chatapp.entity.Message;
import com.eazybytes.chatapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    // Room ke hisaab se sab messages, time ke order me
    List<Message> findByRoomOrderByTimestampAsc(String room);

    // Private messages (unordered)
    List<Message> findBySenderAndReceiver(User sender, User receiver);

    // Private messages (ordered by time)
    List<Message> findBySenderAndReceiverOrderByTimestampAsc(User sender, User receiver);

    // ✅ Room wise messages delete karne ka method
    @Modifying
    @Transactional
    @Query("DELETE FROM Message m WHERE m.room = :room")
    void deleteByRoom(@Param("room") String room);
}
