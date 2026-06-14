package com.example.Nap.Buyzen.controller;

import com.example.Nap.Buyzen.dto.ProductDto;
import com.example.Nap.Buyzen.service.ProductService;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<Page<ProductDto>> getProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize
    ) {

        System.out.println("KEYWORD = [" + keyword + "]");

        Page<ProductDto> result = productService.getProducts(keyword, pageNum, pageSize);

        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/category/{slug}")
    public ResponseEntity<Page<ProductDto>> getProductsByCategorySlug(
            @PathVariable String slug,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize
    ) {

        Page<ProductDto> products =
                productService.getProductsByCategory(slug, pageNum, pageSize);

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
