package com.example.Nap.Buyzen;

import com.example.Nap.Buyzen.dto.SendmessageDto;
import com.example.Nap.Buyzen.entities.Chat;
import com.example.Nap.Buyzen.entities.Message;
import com.example.Nap.Buyzen.entities.User;
import com.example.Nap.Buyzen.repository.ChatRepo;
import com.example.Nap.Buyzen.repository.MessageRepo;
import com.example.Nap.Buyzen.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class MessageTest {

    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private ChatRepo chatRepo;

    @Autowired
    private UserRepo userRepo;

    @Test
    void testMessage() {

        Chat chat = new Chat();
        chat.setSenderId(9);
        chat.setReceiverId(10);
        chat.setCreatedAt(LocalDateTime.now());

        // Save chat so it gets an ID
        chat = chatRepo.save(chat);

        List<Message> messages = new ArrayList<>();

        Message message = new Message();
        message.setChat(chat);
        message.setSenderId(9);
        message.setContent("Hello");
        message.setCreatedAt(
                LocalDateTime.of(2026, 9, 3, 10, 30)
        );
        messages.add(message);

        Message message1 = new Message();
        message1.setChat(chat);
        message1.setSenderId(10);
        message1.setContent("hi");
        message1.setCreatedAt(
                LocalDateTime.of(2026, 9, 3, 10, 30)
        );
        messages.add(message1);

        for (Message m : messages) {

            // Save message
            messageRepo.save(m);
        }


        List<SendmessageDto> messageDtoList =
                messageRepo.findAllByChatId(chat.getId());

        for (SendmessageDto messageDto : messageDtoList) {

            User sender = userRepo.findById(messageDto.senderId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            int receiverId;

            if (messageDto.senderId() == chat.getSenderId()) {
                receiverId = chat.getReceiverId();
            } else {
                receiverId = chat.getSenderId();
            }

            User receiver = userRepo.findById(receiverId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            System.out.println("sender: " + sender.getName());
            System.out.println("message: " + messageDto.content());
            System.out.println("receiver: " + receiver.getName());
        }
    }
}