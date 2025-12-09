package com.productmanagement.model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    @DisplayName("Test Category creation and getters")
    void testCategoryCreation() {
        // Given
        Category category = new Category(1, "Electronics");

        // Then
        assertEquals(1, category.getId());
        assertEquals("Electronics", category.getName());
    }

    @Test
    @DisplayName("Test Category setters")
    void testCategorySetters() {
        // Given
        Category category = new Category();

        // When
        category.setId(2);
        category.setName("Books");

        // Then
        assertEquals(2, category.getId());
        assertEquals("Books", category.getName());
    }

    @Test
    @DisplayName("Test Category toString method")
    void testCategoryToString() {
        // Given
        Category category = new Category(3, "Clothing");

        // When
        String toString = category.toString();

        // Then
        assertTrue(toString.contains("3"));
        assertTrue(toString.contains("Clothing"));
    }
}