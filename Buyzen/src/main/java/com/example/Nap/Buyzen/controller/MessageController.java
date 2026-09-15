package com.example.Nap.Buyzen.controller;

import com.example.Nap.Buyzen.dto.MessageResponseDto;
import com.example.Nap.Buyzen.dto.SendmessageDto;
import com.example.Nap.Buyzen.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;


import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<MessageResponseDto>> getMessages(@PathVariable("chatId") int chatId){
        return ResponseEntity.ok(messageService.getMessages(chatId));
    }

    @MessageMapping("/message")
    public void sendMessage(SendmessageDto messageDto){
       MessageResponseDto saveMessage=messageService.sendMessage(messageDto);
       messagingTemplate.convertAndSend("/topic/messages",saveMessage);
    }


}
