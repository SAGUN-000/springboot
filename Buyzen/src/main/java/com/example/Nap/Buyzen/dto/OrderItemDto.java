package com.example.Nap.Buyzen.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class OrderItemDto {

    private int id;
    private int orderId;
    private int productId;
    private String product_name;
    private BigDecimal price;
    private int quantity;
    private String url;


    public OrderItemDto(int productId) {
        this.productId=productId;
    }
}
