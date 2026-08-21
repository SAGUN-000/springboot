package com.example.Nap.Buyzen.repository;

import com.example.Nap.Buyzen.entities.User;
import com.example.Nap.Buyzen.enums.AuthProviderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Integer> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);


    Optional<User> findByProviderTypeAndProviderId(AuthProviderType providerType, String providerId);
}
