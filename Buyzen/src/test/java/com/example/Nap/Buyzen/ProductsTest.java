package com.example.Nap.Buyzen;

import com.example.Nap.Buyzen.entities.Product;
import com.example.Nap.Buyzen.repository.ProductRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProductsTest {
    @Autowired
    private ProductRepo productRepo;


    @Test
    void testProducts(){
        Product product=productRepo.findByName("t-shirt").orElseThrow(()->new RuntimeException("not found"));
        System.out.println(product.getName());
        System.out.println(product.getPrice());
    }
}


