package com.example.Nap.Buyzen.service;

import com.example.Nap.Buyzen.dto.*;
import com.example.Nap.Buyzen.entities.User;
import com.example.Nap.Buyzen.enums.Role;
import com.example.Nap.Buyzen.repository.UserRepo;
import com.example.Nap.Buyzen.security.AuthUtil;
import com.example.Nap.Buyzen.security.SecurityPrinciple;
import org.apache.catalina.UserDatabase;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;

    private final PasswordEncoder passwordEncoder;

    private int getCurrentUserId() {
        SecurityPrinciple principle = (SecurityPrinciple) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return principle.getUserId(); // ✅ real user from JWT
    }

    public UserService(UserRepo userRepo, AuthenticationManager authenticationManager, AuthUtil authUtil, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.authenticationManager = authenticationManager;
        this.authUtil = authUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public void signup(SignupDto signupDto, Role role){
        if (signupDto==null){
            return;
        }
        if (userRepo.existsByEmail(signupDto.getEmail())){
            throw new RuntimeException("Email already exist");
        }
        String hashedPassword=passwordEncoder.encode(signupDto.getPassword());
        User user=new User(signupDto.getName(),signupDto.getEmail(),hashedPassword,role);
        userRepo.save(user);

    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto){

        Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(),loginRequestDto.getPassword())
        );

        User user=(User) authentication.getPrincipal();
        String token=authUtil.generateAccessToken(user);

        return new LoginResponseDto(token,user.getId());

    }

    public UserDto getUserDetails(){
        int id=getCurrentUserId();
        User user=userRepo.findById(id).orElseThrow(()->new RuntimeException("user not found"));
        return new UserDto(user.getName(),user.getEmail());
    }

    public List<UserDto> getAllUsers(){
       return userRepo.findAll().stream().map(user->(new UserDto(
                user.getId(),user.getName(), user.getEmail(),user.getRole().name()))).toList();
    }


    public void updatePass(PassUpdateDto dto){
        int userid=getCurrentUserId();
        User user=userRepo.findById(userid).orElseThrow(()->new RuntimeException("user not found"));
        if(!passwordEncoder.matches(dto.getOldPass(), user.getPassword())){
            throw new RuntimeException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPass()));
        userRepo.save(user);
    }

    public void deleteUser(int id){
        User user=userRepo.findById(id).orElseThrow(()->new RuntimeException("user not found"));
        userRepo.delete(user);
    }
}
