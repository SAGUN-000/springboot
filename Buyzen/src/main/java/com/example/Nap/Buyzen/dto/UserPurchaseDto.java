package com.example.Nap.Buyzen.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserPurchaseDto {
    private String name;
    private String email;
    private int quantity;
}
