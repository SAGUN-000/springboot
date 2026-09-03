package com.example.Nap.Buyzen;

import com.example.Nap.Buyzen.dto.MessageDto;
import com.example.Nap.Buyzen.entities.Chat;
import com.example.Nap.Buyzen.entities.Message;
import com.example.Nap.Buyzen.repository.ChatRepo;
import com.example.Nap.Buyzen.repository.MessageRepo;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class MessageTest {

    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private ChatRepo chatRepo;

    @Test
    void testMessage() {

        Chat chat = new Chat();
        chat.setSenderId(5);
        chat.setReceiverId(2);
        chat.setCreatedAt(LocalDateTime.now());

        // Save chat so it gets an ID
        chat = chatRepo.save(chat);

        Message message = new Message();
        message.setChat(chat);
        message.setContent("Hello");
        message.setCreatedAt(
                LocalDateTime.of(2026, 9, 3, 10, 30)
        );

        // Save message
        messageRepo.save(message);

        List<MessageDto> messageDtoList =
                messageRepo.findAllByChatId(chat.getId());

        for (MessageDto messageDto : messageDtoList) {
            System.out.println(messageDto);
        }
    }
}