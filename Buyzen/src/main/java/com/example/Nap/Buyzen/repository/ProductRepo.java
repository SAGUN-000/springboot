package com.example.Nap.Buyzen.repository;

import com.example.Nap.Buyzen.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepo extends JpaRepository<Product, Integer> {
    Page<Product> findByNameContainingIgnoreCase(String keyword,  Pageable pageable);
    Optional<Product> findByName(String name);
    Page<Product> findByCategory_Slug(String slug, Pageable pageable);
    @Query("SELECT p FROM Product p WHERE p.featured = true")
    Page<Product>findFeaturedProducts(Pageable pageable);
}
