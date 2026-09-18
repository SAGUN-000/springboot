package com.example.Nap.Buyzen.dto;

import com.example.Nap.Buyzen.dto.AdminOrderItemDto;

import java.time.LocalDateTime;
import java.util.List;

public record AdminOrderDto(
        int orderId,
        int userId,
        String userName,
        String email,
        String status,
        LocalDateTime createdAt,
        List<AdminOrderItemDto> products
) {}