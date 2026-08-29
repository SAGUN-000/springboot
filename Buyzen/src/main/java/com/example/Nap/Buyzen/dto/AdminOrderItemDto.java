package com.example.Nap.Buyzen.dto;

public record AdminOrderItemDto(
        int productId,
        String productName,
        String url,
        int quantity
) {}