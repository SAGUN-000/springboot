package com.example.Nap.Buyzen.service;

import com.example.Nap.Buyzen.dto.ProductDto;
import com.example.Nap.Buyzen.entities.Product;
import com.example.Nap.Buyzen.repository.ProductRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepo productRepo;

    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public Page<ProductDto> getProducts(String keyword, int pageNum, int pageSize) {

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);

        Page<Product> productPage;

        if (keyword == null || keyword.isBlank()) {
            productPage = productRepo.findAll(pageable);
        } else {
            productPage = productRepo.findByNameContainingIgnoreCase(keyword,pageable);
        }

        return productPage.map(p ->
                new ProductDto(
                        p.getId(),
                        p.getName(),
                        p.getPrice(),
                        p.getUrl(),
                        p.isFeatured()
                )
        );
    }


    public Page<ProductDto> getProductsByCategory(String slug, int pageNum, int pageSize) {

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);

        Page<Product> productPage = productRepo.findByCategory_Slug(slug, pageable);

        return productPage.map(p ->
                new ProductDto(
                        p.getId(),
                        p.getName(),
                        p.getPrice(),
                        p.getUrl(),
                        p.getCategory().getId()
                )
        );
    }

    public ProductDto getProductById(int id){


        Product product=productRepo.findById(id).orElseThrow(()->new RuntimeException("no products found"));

        return new ProductDto(product.getId(),product.getName(),
                product.getPrice(),product.getUrl(),product.getCategory().getName(),product.getDescription());
    }
}
