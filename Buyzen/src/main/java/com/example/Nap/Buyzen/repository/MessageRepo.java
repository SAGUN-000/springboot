package com.example.Nap.Buyzen.repository;

import com.example.Nap.Buyzen.dto.SendmessageDto;
import com.example.Nap.Buyzen.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepo extends JpaRepository<Message,Integer> {
    List<SendmessageDto> findAllByChatId(int chatId);
}
