package com.example.Nap.Buyzen.dto;

import com.example.Nap.Buyzen.dto.OrderItemDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutRequestDto {

    private List<OrderItemDto> orderItems;

    private String address;
    private String city;
    private String province;
    private String postalCode;
    private String country;


    private Double latitude;

    private Double longitude;


}