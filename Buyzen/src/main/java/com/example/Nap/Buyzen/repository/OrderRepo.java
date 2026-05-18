package com.example.Nap.Buyzen.repository;

import com.example.Nap.Buyzen.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order,Integer> {
    List<Order> findByUserId(int userId);
}
