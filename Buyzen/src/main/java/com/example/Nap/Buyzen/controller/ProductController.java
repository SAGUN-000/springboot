package com.example.Nap.Buyzen.controller;

import com.example.Nap.Buyzen.dto.ProductDto;
import com.example.Nap.Buyzen.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin("http://localhost:5173")
public class ProductController {

    private final ProductService productService;

    ProductController(ProductService productService){

        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getProducts(@RequestParam(required = false) String keyword) {
        List<ProductDto> productList;

        if (keyword == null || keyword.isBlank()) {
            productList = productService.getAllProducts();
        } else {
            productList = productService.searchProducts(keyword);
        }

        if (productList == null || productList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(productList);
    }

    @GetMapping("/category/{slug}")
    public ResponseEntity<List<ProductDto>>getProductsByCategorySlug(@PathVariable String slug,
                                                                     @RequestParam(required = false) String keyword){
        List<ProductDto>products;

        if (keyword == null || keyword.isBlank()) {
            products = productService.getProductsByCategory(slug);
        } else {
            products = productService.searchProducts(keyword);
        }
        if (products == null || products.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(products);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable int id){
        try {
            return ResponseEntity.ok(productService.getProductById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
