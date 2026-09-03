package com.example.Nap.Buyzen.repository;

import com.example.Nap.Buyzen.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepo extends JpaRepository<Chat,Integer> {
}
