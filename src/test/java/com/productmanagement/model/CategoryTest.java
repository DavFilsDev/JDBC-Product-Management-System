package com.productmanagement.model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    @DisplayName("Test Category creation and getters")
    void testCategoryCreation() {
        Category category = new Category(1, "Electronics");

        assertEquals(1, category.getId());
        assertEquals("Electronics", category.getName());
    }

    @Test
    @DisplayName("Test Category setters")
    void testCategorySetters() {
        Category category = new Category();

        category.setId(2);
        category.setName("Books");

        assertEquals(2, category.getId());
        assertEquals("Books", category.getName());
    }

    @Test
    @DisplayName("Test Category toString method")
    void testCategoryToString() {
        Category category = new Category(3, "Clothing");

        String toString = category.toString();

        assertTrue(toString.contains("3"));
        assertTrue(toString.contains("Clothing"));
    }
}