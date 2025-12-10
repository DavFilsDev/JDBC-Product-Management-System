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

        int offset = (page - 1) * size;

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

                    Timestamp timestamp = rs.getTimestamp("creation_datetime");
                    if (timestamp != null) {
                        product.setCreationDateTime(timestamp.toInstant());
                    }

                    String categoriesStr = rs.getString("categories");
                    if (categoriesStr != null) {
                        Category category = new Category();
                        category.setName(categoriesStr);
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

    public List<Product> getProductsByCriteria(String productName, String categoryName,
                                               Instant creationMin, Instant creationMax) {
        return getProductsByCriteriaInternal(productName, categoryName,
                creationMin, creationMax, 0, 0);
    }

    public List<Product> getProductsByCriteria(String productName, String categoryName,
                                               Instant creationMin, Instant creationMax,
                                               int page, int size) {
        return getProductsByCriteriaInternal(productName, categoryName,
                creationMin, creationMax, page, size);
    }

    private List<Product> getProductsByCriteriaInternal(String productName, String categoryName,
                                                        Instant creationMin, Instant creationMax,
                                                        int page, int size) {
        List<Product> products = new ArrayList<>();

        StringBuilder sqlBuilder = new StringBuilder(
                "SELECT p.id, p.name, p.price, p.creation_datetime, " +
                        "STRING_AGG(pc.name, ', ') as categories " +
                        "FROM product p " +
                        "LEFT JOIN product_category pc ON p.id = pc.product_id " +
                        "WHERE 1=1"
        );

        List<Object> parameters = new ArrayList<>();

        if (productName != null && !productName.trim().isEmpty()) {
            sqlBuilder.append(" AND p.name ILIKE ?");
            parameters.add("%" + productName.trim() + "%");
        }

        if (categoryName != null && !categoryName.trim().isEmpty()) {
            sqlBuilder.append(" AND EXISTS (");
            sqlBuilder.append("   SELECT 1 FROM product_category pc2 ");
            sqlBuilder.append("   WHERE pc2.product_id = p.id ");
            sqlBuilder.append("   AND pc2.name ILIKE ?");
            sqlBuilder.append(" )");
            parameters.add("%" + categoryName.trim() + "%");
        }

        if (creationMin != null) {
            sqlBuilder.append(" AND p.creation_datetime >= ?");
            parameters.add(Timestamp.from(creationMin));
        }

        if (creationMax != null) {
            sqlBuilder.append(" AND p.creation_datetime <= ?");
            parameters.add(Timestamp.from(creationMax));
        }

        sqlBuilder.append(" GROUP BY p.id, p.name, p.price, p.creation_datetime");

        sqlBuilder.append(" ORDER BY p.id");

        boolean usePagination = page > 0 && size > 0;
        if (usePagination) {
            int offset = (page - 1) * size;
            sqlBuilder.append(" LIMIT ? OFFSET ?");
            parameters.add(size);
            parameters.add(offset);
        }

        String sql = sqlBuilder.toString();

        try (Connection conn = dbConnection.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < parameters.size(); i++) {
                pstmt.setObject(i + 1, parameters.get(i));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error in getProductsByCriteria: " + e.getMessage());
            e.printStackTrace();
        }

        return products;
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setName(rs.getString("name"));
        product.setPrice(rs.getDouble("price"));

        Timestamp timestamp = rs.getTimestamp("creation_datetime");
        if (timestamp != null) {
            product.setCreationDateTime(timestamp.toInstant());
        }

        String categoriesStr = rs.getString("categories");
        if (categoriesStr != null) {
            Category category = new Category();
            category.setName(categoriesStr);
            product.setCategory(category);
        } else {
            product.setCategory(new Category(0, "No Category"));
        }

        return product;
    }
}