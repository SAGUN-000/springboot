package com.example.Nap.Buyzen.repository;

import com.example.Nap.Buyzen.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepo extends JpaRepository<Product, Integer> {
    List<Product> findByNameContainingIgnoreCase(String keyword);
    Optional<Product> findByName(String name);
    List<Product> findByCategory_Slug(String slug);
    @Query("SELECT p FROM Product p WHERE p.featured = true")
    List<Product> findFeaturedProducts();
}
