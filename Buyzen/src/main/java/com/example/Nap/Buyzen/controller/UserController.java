package com.example.Nap.Buyzen.controller;

import com.example.Nap.Buyzen.dto.*;
import com.example.Nap.Buyzen.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;


@Controller
@CrossOrigin("http://localhost:5173")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String>signup(@RequestBody SignupDto signupDto){
        userService.signup(signupDto);

        return ResponseEntity.status(HttpStatus.CREATED).body("user created");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto>login(@RequestBody LoginRequestDto loginRequestDto){
        LoginResponseDto loginResponseDto=userService.login(loginRequestDto);
        if (loginResponseDto==null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(userService.login(loginRequestDto));
    }

    @GetMapping("/user/profile")
    public ResponseEntity<UserDto> getUserDetails(){
        UserDto userDto=userService.getUserDetails();
        if (userDto==null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(userDto);
    }

    @PatchMapping("/user/password")
    public ResponseEntity<String> updatePassword(
            @RequestBody PassUpdateDto dto
    ){
        userService.updatePass(dto);
        return ResponseEntity.ok("new password set successfully");
    }


}
