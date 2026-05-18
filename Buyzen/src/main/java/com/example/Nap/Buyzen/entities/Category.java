package com.example.Nap.Buyzen.entities;
import jakarta.persistence.*;
import lombok.*;


import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false,unique = true)
    private String name;

    @Column(nullable = false,unique = true)
    private String slug;

    @OneToMany(mappedBy = "category")
    private Set<Product> products;
}
