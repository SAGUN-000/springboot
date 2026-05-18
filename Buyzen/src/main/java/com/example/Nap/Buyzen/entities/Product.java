package com.example.Nap.Buyzen.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Check(constraints = "stock >= 0")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double price;
    private String description;

    @Column(nullable = false)
    private String url;
    private boolean featured;

    @ManyToOne
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;

    @Column(nullable = false)
    private int stock;
}
