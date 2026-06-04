package com.example.Nap.Buyzen.repository;

import com.example.Nap.Buyzen.dto.UserPurchaseDto;
import com.example.Nap.Buyzen.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepo extends JpaRepository<OrderItem,Integer> {

    @Query("""
        SELECT new com.example.dto.UserPurchaseStatsDto(
            u.name,
            u.email,
            SUM(oi.quantity)
        )
        FROM OrderItem oi
        JOIN oi.order o
        JOIN o.user u
        GROUP BY u.name, u.email
        """)
    List<UserPurchaseDto>getUserPurchaseStats();
}
