package com.example.Nap.Buyzen.config;

import com.example.Nap.Buyzen.entities.User;
import com.example.Nap.Buyzen.enums.Role;
import com.example.Nap.Buyzen.repository.UserRepo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
public class AdminSeeder {

    @Bean
    public CommandLineRunner commandLineRunner(UserRepo  userRepo, PasswordEncoder passwordEncoder) {

        return args -> {
            String adminEmail =  "Admin362@gmail.com";
           Optional<User>existingAdmin=userRepo.findByEmail(adminEmail);
           if(existingAdmin.isEmpty()){
               User admin = new User();
               admin.setName("Admin");
               admin.setEmail(adminEmail);
               admin.setPassword(passwordEncoder.encode("admin128270"));
               admin.setRole(Role.ADMIN);
               userRepo.save(admin);
               System.out.printf("Admin has been saved with email: %s%n",admin.getEmail());
           }

        };
    }
}
