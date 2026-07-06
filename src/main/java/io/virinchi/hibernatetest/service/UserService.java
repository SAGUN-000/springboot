package io.virinchi.hibernatetest.service;

import io.virinchi.hibernatetest.Repository.UserRepo;
import io.virinchi.hibernatetest.dto.UserDto;
import io.virinchi.hibernatetest.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    public void SaveUser(UserDto userDto) {
        User user = new User(userDto.getUsername(), userDto.getEmail(), userDto.getPassword());
        userRepo.save(user);
        System.out.println("User saved successfully");

    }



}
