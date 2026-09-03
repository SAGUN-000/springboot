package com.example.Nap.Buyzen.controller;

import com.example.Nap.Buyzen.dto.MessageDto;
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

    public ResponseEntity<Integer> createMessage(){
       return ResponseEntity.ok(messageService.createChat());
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<MessageDto>> getMessages(@PathVariable("chatId") int chatId){
        return ResponseEntity.ok(messageService.getMessages(chatId));
    }

    @MessageMapping("/message")
    public void sendMessage(MessageDto messageDto){
       MessageDto saveMessage=messageService.saveMessage(messageDto);
       messagingTemplate.convertAndSend("/topic/messages",saveMessage);
    }


}
