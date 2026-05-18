package com.example.Nap.Buyzen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemsDto {

    private int cartId;     // links back to the cart
    private int id;
    private String name;
    private Integer quantity;
    private double price;
    private String url;
    private double subtotal;
}
