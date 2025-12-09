package com.productmanagement.model;

import org.junit.jupiter.api.*;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    @DisplayName("Test Product creation and getters")
    void testProductCreation() {
        Category category = new Category(1, "Electronics");
        Instant now = Instant.now();
        Product product = new Product(1, "Test Product", 99.99, now, category);

        assertEquals(1, product.getId());
        assertEquals("Test Product", product.getName());
        assertEquals(99.99, product.getPrice(), 0.001);
        assertEquals(now, product.getCreationDateTime());
        assertEquals(category, product.getCategory());
    }

    @Test
    @DisplayName("Test getCategoryName when category exists")
    void testGetCategoryName_WithCategory() {
        Category category = new Category(1, "Electronics");
        Product product = new Product(1, "Test", 10.0, Instant.now(), category);

        assertEquals("Electronics", product.getCategoryName());
    }

    @Test
    @DisplayName("Test getCategoryName when category is null")
    void testGetCategoryName_WithoutCategory() {
        Product product = new Product();
        product.setId(1);
        product.setName("Test");

        assertEquals("N/A", product.getCategoryName());
    }

    @Test
    @DisplayName("Test Product setters")
    void testProductSetters() {
        Product product = new Product();
        Category category = new Category(2, "Books");
        Instant newTime = Instant.now().plusSeconds(3600);

        product.setId(2);
        product.setName("New Product");
        product.setPrice(49.99);
        product.setCreationDateTime(newTime);
        product.setCategory(category);

        assertEquals(2, product.getId());
        assertEquals("New Product", product.getName());
        assertEquals(49.99, product.getPrice(), 0.001);
        assertEquals(newTime, product.getCreationDateTime());
        assertEquals(category, product.getCategory());
    }
}