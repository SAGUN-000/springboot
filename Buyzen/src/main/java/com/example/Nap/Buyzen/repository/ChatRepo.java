package com.example.Nap.Buyzen.repository;

import com.example.Nap.Buyzen.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRepo extends JpaRepository<Chat,Integer> {

    @Query("""
        SELECT c FROM Chat c
        WHERE (c.senderId = :user1 AND c.receiverId = :user2)
           OR (c.senderId = :user2 AND c.receiverId = :user1)
    """)
    Optional<Chat> findChatBetweenUsers(
            @Param("user1") int user1,
            @Param("user2") int user2
    );
}
