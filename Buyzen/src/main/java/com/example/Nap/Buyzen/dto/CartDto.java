package com.example.Nap.Buyzen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {

    private int cartId;
    private int userId;

    private List<CartItemsDto> cartItemsDtoList;
    private double subtotal;


    public <T> CartDto(List<T> ts) {
    }
}

