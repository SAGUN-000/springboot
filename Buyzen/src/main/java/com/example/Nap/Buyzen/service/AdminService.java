package com.example.Nap.Buyzen.service;

import com.example.Nap.Buyzen.dto.SignupDto;
import com.example.Nap.Buyzen.entities.CartItems;
import com.example.Nap.Buyzen.entities.User;
import com.example.Nap.Buyzen.enums.Role;
import com.example.Nap.Buyzen.repository.CartItemRepo;
import com.example.Nap.Buyzen.repository.CartRepo;
import com.example.Nap.Buyzen.repository.ProductRepo;
import com.example.Nap.Buyzen.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class AdminService {

    private final UserRepo userRepo;
    private final ProductRepo productRepo;
    private final CartRepo cartRepo;
    private final CartItemRepo cartItemRepo;
    private final PasswordEncoder passwordEncoder;

    public void SignUp(SignupDto signupDto){
        if (signupDto==null){
            throw new RuntimeException("empty signDto");
        }
        String EncodedPass=passwordEncoder.encode(signupDto.getPassword());
        String role= String.valueOf(Role.ADMIN);
        User user=new User(signupDto.getName(), signupDto.getEmail(),EncodedPass,role);
        userRepo.save(user);

    }



}
