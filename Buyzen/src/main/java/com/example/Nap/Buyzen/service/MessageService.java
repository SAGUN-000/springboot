package com.example.Nap.Buyzen.service;

import com.example.Nap.Buyzen.dto.MessageDto;
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

    public Integer createChat(){

        int userId = getCurrentUserId();

         //check whether a chat exist or not

        Chat chat = chatRepo.findById(userId).orElse(null);

        if (chat == null) {
            User user=userRepo.findById(userId).orElseThrow(()->new RuntimeException("user not found"));
            User admin = userRepo.findByRole(Role.ADMIN)
                    .orElseThrow(() -> new RuntimeException("Admin not found"));
            chat = new Chat();
            chat.setSenderId(userId);
            chat.setReceiverId(admin.getId());
            chatRepo.save(chat);
        }
         return chat.getId();
    }

    public List<MessageDto> getMessages(int chatId) {

        //check whether chat exists or not

        chatRepo.findById(chatId).orElseThrow(()->new RuntimeException("chat not found"));

        return messageRepo.findAllByChatId(chatId);
    }

    public MessageDto saveMessage(MessageDto messageDto) {
        Chat chat= chatRepo.findById(messageDto.chatId()).orElseThrow(()->new RuntimeException("chat not found"));

        Message message = new Message();
        message.setChat(chat);
        message.setContent(messageDto.content());
        Message savedMessage= messageRepo.save(message);

        return new MessageDto(savedMessage.getChat().getId(),savedMessage.getContent());
    }
}
