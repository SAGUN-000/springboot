package com.example.Nap.Buyzen.controller;

import com.example.Nap.Buyzen.dto.ChatHistoryResponseDto;
import com.example.Nap.Buyzen.dto.MessageResponseDto;
import com.example.Nap.Buyzen.dto.SendmessageDto;
import com.example.Nap.Buyzen.security.SecurityPrinciple;
import com.example.Nap.Buyzen.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;


import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class MessageController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;


    @GetMapping("/{receiverId}")
    public ResponseEntity<Integer>getChatId(@PathVariable int receiverId){
      return   ResponseEntity.ok(messageService.getChatIdByReceiverId(receiverId));
    }

    @GetMapping("/{chatId}/messages")
    public ResponseEntity<ChatHistoryResponseDto> getMessages(
            @PathVariable int chatId
    ) {
        return ResponseEntity.ok(messageService.getMessages(chatId));
    }


    @MessageMapping("/message")
    public void sendMessage(
            SendmessageDto messageDto,
            @Header("simpUser") Principal principal
    ) {

        Authentication authentication =
                (Authentication) principal;

        SecurityPrinciple securityPrinciple =
                (SecurityPrinciple) authentication.getPrincipal();

        MessageResponseDto savedMessage =
                messageService.sendMessage(
                        messageDto,
                        securityPrinciple.getUserId()
                );

        messagingTemplate.convertAndSend(
                "/topic/messages",
                savedMessage
        );
    }

}
