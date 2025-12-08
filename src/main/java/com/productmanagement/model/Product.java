package com.productmanagement.model;

import java.time.Instant;

public class Product {
    private int id;
    private String name;
    private double price;
    private Instant creationDateTime;
    private Category category;

    public Product(int id, String name, double price, Instant creationDateTime, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.creationDateTime = creationDateTime;
        this.category = category;
    }

    public String getCategoryName() {
        return category != null ? category.getName() : "N/A";
    }
}