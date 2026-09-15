package com.example.Nap.Buyzen.service;

import com.example.Nap.Buyzen.dto.MessageResponseDto;
import com.example.Nap.Buyzen.dto.SendmessageDto;
import com.example.Nap.Buyzen.dto.UserChatsDto;
import com.example.Nap.Buyzen.entities.Chat;
import com.example.Nap.Buyzen.entities.Message;
import com.example.Nap.Buyzen.entities.User;
import com.example.Nap.Buyzen.enums.Role;
import com.example.Nap.Buyzen.repository.ChatRepo;
import com.example.Nap.Buyzen.repository.MessageRepo;
import com.example.Nap.Buyzen.repository.UserRepo;
import com.example.Nap.Buyzen.security.SecurityPrinciple;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class MessageService {


    private final ChatRepo chatRepo;
    private final MessageRepo messageRepo;
    private final UserRepo userRepo;


    private int getCurrentUserId() {
        SecurityPrinciple principle = (SecurityPrinciple) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return principle.getUserId(); // ✅ real user from JWT
    }


    public List<MessageResponseDto> getMessages(int chatId) {

        //check whether chat exists or not

        return messageRepo.findAllByChatId(chatId)
                .stream()
                .map(message -> new MessageResponseDto(
                        message.getChat().getId(),
                        message.getSenderId(),
                        message.getContent()
                ))
                .toList();
    }

    public Integer getChatIdByReceiverId(int receiverId) {
        int senderId = getCurrentUserId();
        return chatRepo.findChatBetweenUsers(senderId, receiverId)
                .map(Chat::getId)
                .orElse(null);

    }


    public MessageResponseDto sendMessage(SendmessageDto messageDto) {

        int senderId = getCurrentUserId();

        userRepo.findById(senderId).orElseThrow(() -> new RuntimeException("sender not found"));

        // Find chat between sender and receiver
        Chat chat = chatRepo
                .findChatBetweenUsers(senderId, messageDto.receiverId())
                .orElseGet(() -> {
                    Chat newChat = new Chat();
                    newChat.setSenderId(senderId);
                    newChat.setReceiverId(messageDto.receiverId());
                    return chatRepo.save(newChat);
                });

        Message message = new Message();
        message.setChat(chat);
        message.setSenderId(senderId);
        message.setContent(messageDto.content());

        messageRepo.save(message);

        return new MessageResponseDto(
                chat.getId(),
                senderId,
                messageDto.content()
        );
    }

    public List<UserChatsDto> getAllUsersWithChats() {

        User admin = userRepo.findByRole(Role.ADMIN)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        return chatRepo.findUsersWithRecentMessage(admin.getId());
    }
}



