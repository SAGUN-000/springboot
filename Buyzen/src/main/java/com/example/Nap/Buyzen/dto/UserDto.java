package com.example.Nap.Buyzen.dto;

import com.example.Nap.Buyzen.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private int id;
    private String username;
    private String email;
    private String role;



    public UserDto(String name, String email) {
    }
}
