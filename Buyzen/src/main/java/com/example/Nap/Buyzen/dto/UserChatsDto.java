package com.example.Nap.Buyzen.dto;

public record UserChatsDto(
        int chatId,
        int userId,
        String name,
        String recentMessage
) {
}
