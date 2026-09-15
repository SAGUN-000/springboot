package com.example.Nap.Buyzen;



import com.example.Nap.Buyzen.dto.UserChatsDto;
import com.example.Nap.Buyzen.entities.Chat;
import com.example.Nap.Buyzen.entities.Message;
import com.example.Nap.Buyzen.repository.ChatRepo;
import com.example.Nap.Buyzen.repository.MessageRepo;
import com.example.Nap.Buyzen.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminChatTest {

    @Autowired
    private MessageService messageService;

    @Autowired
    private ChatRepo chatRepo;

    @Autowired
    private MessageRepo messageRepo;

    @BeforeEach
    void seedData() {

        messageRepo.deleteAll();
        chatRepo.deleteAll();

        // Admin = 1
        // User = 2

        Chat chat = new Chat();
        chat.setSenderId(1);
        chat.setReceiverId(2);

        chat = chatRepo.save(chat);

        Message message1 = new Message();
        message1.setChat(chat);
        message1.setSenderId(2);
        message1.setContent("Hello admin!");

        Message message2 = new Message();
        message2.setChat(chat);
        message2.setSenderId(1);
        message2.setContent("Hello! How can I help you?");

        Message message3 = new Message();
        message3.setChat(chat);
        message3.setSenderId(2);
        message3.setContent("I want to know about my order.");

        messageRepo.saveAll(
                List.of(
                        message1,
                        message2,
                        message3
                )
        );
    }

    @Test
    void shouldGetAllUsersWithChats() {

        List<UserChatsDto> result =
                messageService.getAllUsersWithChats();

        // Print everything returned by the service
        System.out.println("\n========== ADMIN CHAT USERS ==========");

        for (UserChatsDto userChat : result) {
            System.out.println(
                    "CHAT ID: " + userChat.chatId() +
                    "User ID: " + userChat.userId()
                            + " | Name: " + userChat.name()
                            + " | Recent Message: " + userChat.recentMessage()
            );
        }

        System.out.println("======================================\n");

        assertFalse(result.isEmpty());
    }
}

