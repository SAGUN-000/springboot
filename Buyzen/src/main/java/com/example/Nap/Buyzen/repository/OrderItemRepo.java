package com.example.Nap.Buyzen.repository;

import com.example.Nap.Buyzen.dto.UserPurchaseDto;
import com.example.Nap.Buyzen.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepo extends JpaRepository<OrderItem,Integer> {

    @Query("""

            SELECT new com.example.Nap.Buyzen.dto.UserPurchaseDto(
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

    @Query("""
    SELECT oi
    FROM OrderItem oi
    JOIN FETCH oi.order o
    JOIN FETCH o.user u
    JOIN FETCH oi.product p
    ORDER BY o.id DESC
    """)
    List<OrderItem> getAllOrderItemsForAdmin();
}
