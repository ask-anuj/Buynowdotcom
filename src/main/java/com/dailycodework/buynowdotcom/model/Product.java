package com.dailycodework.buynowdotcom.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity

public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment strategy
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;

    @ManyToOne(cascade = CascadeType.ALL) // Establishes many-to-one relationship with Category
    @JoinColumn(name = "category_id") // Foreign key column
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true) // One-to-many relationship with Image
    private List<Image> images;

    public Product(String name, String brand, BigDecimal price, int inventory, String description, Category category) {
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.inventory = inventory;
        this.description = description;
        this.category = category;
        // exclude image and id from constructor
    }
}
