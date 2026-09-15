package com.example.Nap.Buyzen.repository;

import com.example.Nap.Buyzen.dto.UserChatsDto;
import com.example.Nap.Buyzen.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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


    @Query("""
    SELECT new com.example.Nap.Buyzen.dto.UserChatsDto(
        c.id,
        CASE
            WHEN c.senderId = :adminId THEN c.receiverId
            ELSE c.senderId
        END,
        u.name,
        m.content
    )
    FROM Chat c
    JOIN User u ON u.id =
        CASE
            WHEN c.senderId = :adminId THEN c.receiverId
            ELSE c.senderId
        END
    JOIN Message m ON m.chat = c
    WHERE (c.senderId = :adminId OR c.receiverId = :adminId)
      AND m.createdAt = (
          SELECT MAX(m2.createdAt)
          FROM Message m2
          WHERE m2.chat = c
      )
    ORDER BY m.createdAt DESC
    """)
    List<UserChatsDto> findUsersWithRecentMessage(
            @Param("adminId") int adminId
    );


}
