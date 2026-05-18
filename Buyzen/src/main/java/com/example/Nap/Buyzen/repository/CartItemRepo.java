package com.example.Nap.Buyzen.repository;

import com.example.Nap.Buyzen.entities.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepo extends JpaRepository<CartItems,Integer> {
    Optional<CartItems>findByCartIdAndProductId(int cartId, int productId);
    List<CartItems> findByCart_User_IdAndProduct_IdIn(
            int userId,
            List<Integer> productIds
    );

}
