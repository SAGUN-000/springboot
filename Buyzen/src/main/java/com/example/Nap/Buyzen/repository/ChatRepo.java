package com.example.Nap.Buyzen.repository;

import com.example.Nap.Buyzen.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRepo extends JpaRepository<Chat,Integer> {

    Optional<Chat> findChatBetweenUsers(int senderId, int receiverId);
}
