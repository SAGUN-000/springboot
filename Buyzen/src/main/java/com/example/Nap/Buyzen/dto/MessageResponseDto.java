package com.example.Nap.Buyzen.dto;

public record MessageResponseDto(
        int chatId,
        int senderId,
        String content
) {}