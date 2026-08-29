package com.example.Nap.Buyzen.controller;

import com.example.Nap.Buyzen.dto.AdminOrderDto;
import com.example.Nap.Buyzen.dto.SignupDto;
import com.example.Nap.Buyzen.dto.UserDto;
import com.example.Nap.Buyzen.dto.UserPurchaseDto;
import com.example.Nap.Buyzen.enums.Role;
import com.example.Nap.Buyzen.service.AdminAnalyticsService;
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
    private final AdminAnalyticsService adminAnalyticsService;

    @PostMapping("/create_admin")
    public ResponseEntity<String> CreateAdmin(@RequestBody SignupDto signupDto){
        userService.signup(signupDto,Role.ADMIN);
        return ResponseEntity.ok("Admin created");
    }

    @GetMapping("/view_users")
    public ResponseEntity<List<UserDto>> getAllUsers(){
         return ResponseEntity.ok(userService.getAllUsers());
    }

    @DeleteMapping("/delete_user/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable int id){
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted");

    }

    @GetMapping("/user_purchase")
    public ResponseEntity<List<UserPurchaseDto>> getAllUserPurchases(){
        return ResponseEntity.ok(adminAnalyticsService.getUserPurchaseStats());
    }

     @GetMapping("/orderDetails")
    public ResponseEntity<List<AdminOrderDto>> getAllOrders(){
        return ResponseEntity.ok(adminAnalyticsService.getAllOrdersForAdmin());
     }



}
