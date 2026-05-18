package com.example.Nap.Buyzen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private int id;
    private String name;
    private double price;
    private String description;
    private String url;
    private String category;
    private int categoryId;
    private boolean featured;

    public ProductDto(int id,String name, double price, String url,String category,String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.url = url;
        this.category = category;
        this.description=description;
    }

    public ProductDto(int id, String name, double price, String url, int categoryId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.url = url;
        this.categoryId = categoryId;
    }

    public ProductDto(int id, String name, double price,  String url,  boolean featured) {
        this.id=id;
        this.name=name;
        this.price=price;
        this.url=url;
        this.featured=featured;
    }


}
