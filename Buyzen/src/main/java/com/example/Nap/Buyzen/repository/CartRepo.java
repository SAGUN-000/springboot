package com.example.Nap.Buyzen.repository;

import com.example.Nap.Buyzen.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart,Integer> {
    Optional<Cart> findByUserId(int userId);
}
