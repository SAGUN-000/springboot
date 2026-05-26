package com.example.Nap.Buyzen.controller;

import com.example.Nap.Buyzen.dto.SignupDto;
import com.example.Nap.Buyzen.dto.UserDto;
import com.example.Nap.Buyzen.enums.Role;
import com.example.Nap.Buyzen.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@CrossOrigin("http://localhost:5173")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;

    @PostMapping("/create_admin")
    public ResponseEntity<String> CreateAdmin(@RequestBody SignupDto signupDto){
        userService.signup(signupDto,Role.ADMIN);
        return ResponseEntity.ok("Admin created");
    }

    @GetMapping("/view_users")
    public ResponseEntity<List<UserDto>> getAllUsers(){
         return ResponseEntity.ok(userService.getAllUsers());
    }





}
