package com.example.Nap.Buyzen.dto;

import com.example.Nap.Buyzen.enums.OrderStatus;

public record StatusUpdateRequestDto(
        OrderStatus status
) {}
