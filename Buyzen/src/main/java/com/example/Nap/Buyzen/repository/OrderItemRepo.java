package com.example.Nap.Buyzen.repository;

import com.example.Nap.Buyzen.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepo extends JpaRepository<OrderItem,Integer> {
}
