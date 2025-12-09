package com.productmanagement.dao;

import com.productmanagement.DBConnection;
import com.productmanagement.model.Category;
import com.productmanagement.model.Product;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {
    private DBConnection dbConnection;

    public DataRetriever() {
        this.dbConnection = new DBConnection();
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM product_category ORDER BY name";

        try (Connection conn = dbConnection.getDBConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Category category = new Category();
                category.setId(rs.getInt("id"));
                category.setName(rs.getString("name"));
                categories.add(category);
            }

        } catch (SQLException e) {
            System.err.println("Error getting categories: " + e.getMessage());
        }

        return categories;
    }

    public List<Product> getProductList(int page, int size) {
        List<Product> products = new ArrayList<>();

        // Calculate offset
        int offset = (page - 1) * size;

        // SQL for your schema (products with multiple categories)
        String sql = "SELECT " +
                "    p.id as product_id, " +
                "    p.name as product_name, " +
                "    p.price, " +
                "    p.creation_datetime, " +
                "    STRING_AGG(pc.name, ', ') as categories " +
                "FROM product p " +
                "LEFT JOIN product_category pc ON p.id = pc.product_id " +
                "GROUP BY p.id, p.name, p.price, p.creation_datetime " +
                "ORDER BY p.id " +
                "LIMIT ? OFFSET ?";

        try (Connection conn = dbConnection.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, size);
            pstmt.setInt(2, offset);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product();
                    product.setId(rs.getInt("product_id"));
                    product.setName(rs.getString("product_name"));
                    product.setPrice(rs.getDouble("price"));

                    // Handle timestamp
                    Timestamp timestamp = rs.getTimestamp("creation_datetime");
                    if (timestamp != null) {
                        product.setCreationDateTime(timestamp.toInstant());
                    }

                    // Handle categories (as comma-separated string)
                    String categoriesStr = rs.getString("categories");
                    if (categoriesStr != null) {
                        // Create a Category object with all categories concatenated
                        // Or if you want to store them separately, you'll need a List<Category>
                        Category category = new Category();
                        category.setName(categoriesStr); // All categories as one string
                        product.setCategory(category);
                    }

                    products.add(product);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error getting product list: " + e.getMessage());
        }

        return products;
    }

}