package io.virinchi.hibernatetest.controller;

import io.virinchi.hibernatetest.dto.UserDto;
import io.virinchi.hibernatetest.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping("/signup")
    public String signUp(UserDto userDto) {
        userService.SaveUser(userDto);
        return "login success";
    }

}
