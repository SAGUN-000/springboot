package com.example.Nap.Buyzen.dto;

import com.example.Nap.Buyzen.dto.AdminOrderItemDto;

import java.util.List;

public record AdminOrderDto(
        int orderId,
        int userId,
        String userName,
        String email,
        String status,
        List<AdminOrderItemDto> products
) {}