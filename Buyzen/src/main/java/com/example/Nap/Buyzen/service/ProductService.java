package com.example.Nap.Buyzen.service;

import com.example.Nap.Buyzen.dto.ProductDto;
import com.example.Nap.Buyzen.entities.Product;
import com.example.Nap.Buyzen.repository.ProductRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepo productRepo;

    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public List<ProductDto> getAllProducts(){
        List<Product>productList=productRepo.findFeaturedProducts();
        if (productList.isEmpty()){
            return null;
        }
        return productList.stream().map(p->(new ProductDto(p.getId(),p.getName(),p.getPrice()
        ,p.getUrl(),p.isFeatured()))).toList();
    }

    public List<ProductDto> searchProducts(String keyword){
        List<Product> productList=productRepo.findByNameContainingIgnoreCase(keyword);
        return productList.stream().map((p)->new ProductDto(p.getId(),p.getName()
        ,p.getPrice(),p.getUrl(),p.getCategory().getId())).toList();
    }

    public List<ProductDto>getProductsByCategory(String slug){
        List<Product>productList=productRepo.findByCategory_Slug(slug);
        return productList.stream().map((p)->new ProductDto(p.getId(),p.getName(),p.getPrice(),
                p.getUrl(),p.getCategory().getId())).toList();
    }

    public ProductDto getProductById(int id){


        Product product=productRepo.findById(id).orElseThrow(()->new RuntimeException("no products found"));

        return new ProductDto(product.getId(),product.getName(),
                product.getPrice(),product.getUrl(),product.getCategory().getName(),product.getDescription());
    }
}
