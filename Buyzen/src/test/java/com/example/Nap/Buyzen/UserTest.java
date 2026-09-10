package com.example.Nap.Buyzen;

import com.example.Nap.Buyzen.dto.UserDto;
import com.example.Nap.Buyzen.entities.User;
import com.example.Nap.Buyzen.repository.UserRepo;
import com.example.Nap.Buyzen.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest

public class UserTest {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    @Test
    public void getUserDetails() {
       UserDto userDto= userService.getUserDetails();

        System.out.println(userDto.getUsername());
        System.out.println(userDto.getEmail());
        System.out.println(userDto.getRole());
        System.out.println(userDto.getAuthProviderType());
    }
}
