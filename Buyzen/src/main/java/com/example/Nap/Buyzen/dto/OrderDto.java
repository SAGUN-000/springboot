package com.example.Nap.Buyzen.dto;

import com.example.Nap.Buyzen.enums.OrderStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private int id;
    private OrderStatus status;
    private List<OrderItemDto> items = new ArrayList<>();
    private BigDecimal totalPrice;

    public <T> OrderDto(List<T> ts) {
    }
}
