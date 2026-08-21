package com.example.Nap.Buyzen.service;

import com.example.Nap.Buyzen.dto.*;
import com.example.Nap.Buyzen.entities.User;
import com.example.Nap.Buyzen.enums.AuthProviderType;
import com.example.Nap.Buyzen.enums.Role;
import com.example.Nap.Buyzen.repository.UserRepo;
import com.example.Nap.Buyzen.security.AuthUtil;
import com.example.Nap.Buyzen.security.SecurityPrinciple;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
    private final JavaMailSender javaMailSender;

    private int getCurrentUserId() {
        SecurityPrinciple principle = (SecurityPrinciple) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return principle.getUserId(); // ✅ real user from JWT
    }

    public UserService(UserRepo userRepo, AuthenticationManager authenticationManager, AuthUtil authUtil, PasswordEncoder passwordEncoder, JavaMailSender javaMailSender) {
        this.userRepo = userRepo;
        this.authenticationManager = authenticationManager;
        this.authUtil = authUtil;
        this.passwordEncoder = passwordEncoder;
        this.javaMailSender = javaMailSender;
    }

    public void sendMail(String to) {

        try {
            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Welcome to Buyzen!");

            String htmlContent = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            background-color: #f4f4f4;
                            padding: 40px;
                        }

                        .container {
                            max-width: 600px;
                            margin: auto;
                            background-color: white;
                            padding: 30px;
                            border-radius: 10px;
                        }

                        h1 {
                            color: #333333;
                        }

                        p {
                            color: #555555;
                            font-size: 16px;
                            line-height: 1.6;
                        }

                        .footer {
                            margin-top: 30px;
                            font-size: 13px;
                            color: #999999;
                        }
                    </style>
                </head>

                <body>
                    <div class="container">
                        <h1>Welcome to Buyzen! 🎉</h1>

                        <p>
                            Your account has been successfully created.
                        </p>

                        <p>
                            Thanks for registering with Buyzen. You can now
                            log in and start using the platform.
                        </p>

                        <div class="footer">
                            <p>
                                This is an automated email. Please do not reply.
                            </p>
                            <p>© 2026 Buyzen</p>
                        </div>
                    </div>
                </body>
                </html>
                """;

            helper.setText(htmlContent, true);

            javaMailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send registration email", e);
        }
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
        user.setProviderType(AuthProviderType.LOCAL);
        userRepo.save(user);
        sendMail(user.getEmail());

    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto){

        User user = userRepo.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (user.getProviderType() != AuthProviderType.LOCAL) {
            throw new RuntimeException("This account uses " + user.getProviderType()
                    + " authentication");
        }


        Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(),loginRequestDto.getPassword())
        );

         user=(User) authentication.getPrincipal();
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

        if (user.getProviderType() != AuthProviderType.LOCAL) {
            throw new RuntimeException("Password authentication is not enabled for this account");
        }

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
