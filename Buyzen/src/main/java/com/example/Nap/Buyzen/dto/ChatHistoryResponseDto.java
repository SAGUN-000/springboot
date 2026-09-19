package com.example.Nap.Buyzen.dto;

import java.util.List;

public record ChatHistoryResponseDto(
        int chatId,
        int receiverId,
        List<MessageResponseDto> messages
) {}