package com.productmanagement;

import com.productmanagement.model.Category;
import com.productmanagement.model.Product;
import com.productmanagement.dao.DataRetriever;
import java.util.List;

public class TestMain {
    public static void main(String[] args) {
        System.out.println("=== Testing Database Connection and Categories ===\n");

        // Step 1: Test DBConnection
        System.out.println("1. Testing DBConnection...");
        DBConnection dbConnection = new DBConnection();

        try {
            // Try to get a connection
            var conn = dbConnection.getDBConnection();
            System.out.println("✅ DBConnection successful!");
            System.out.println("   Database: " + conn.getMetaData().getDatabaseProductName());
            System.out.println("   URL: " + conn.getMetaData().getURL());
            conn.close();
        } catch (Exception e) {
            System.err.println("❌ DBConnection failed: " + e.getMessage());
            System.err.println("Please check:");
            System.err.println("   - Is PostgreSQL running?");
            System.err.println("   - Is database 'product_management_db' created?");
            System.err.println("   - Check database.properties file");
            return; // Stop if connection fails
        }

        // Step 2: Test DataRetriever - getAllCategories()
        System.out.println("\n2. Testing getAllCategories()...");
        DataRetriever dataRetriever = new DataRetriever();

        List<Category> categories = dataRetriever.getAllCategories();

        if (categories.isEmpty()) {
            System.out.println("❌ No categories found! The database might be empty.");
            System.out.println("Please run the SQL script to insert sample data.");
        } else {
            System.out.println("✅ Found " + categories.size() + " categories:");
            System.out.println("--------------------------------");
            for (int i = 0; i < categories.size(); i++) {
                Category cat = categories.get(i);
                System.out.printf("%2d. ID: %d | Name: %s%n",
                        (i + 1), cat.getId(), cat.getName());
            }
            System.out.println("--------------------------------");
        }

        // Step 3: Test Pagination - getProductList()
        System.out.println("\n3. Testing Pagination - getProductList()...");
        System.out.println("===========================================");

        System.out.println("\nTest 1: Page 5, Size 1");
        System.out.println("Expected: Last product");
        List<Product> page5 = dataRetriever.getProductList(5, 1);
        displayProducts(page5);

        System.out.println("\nTest 2: Page 2, Size 2");
        System.out.println("Expected: Next 2 products (products 3 and 4)");
        List<Product> page2 = dataRetriever.getProductList(2, 2);
        displayProducts(page2);

        System.out.println("\n=== Test Complete ===");
    }

    /**
     * Helper method to display products nicely
     */
    private static void displayProducts(List<Product> products) {
        if (products == null || products.isEmpty()) {
            System.out.println("   No products found");
            return;
        }

        System.out.println("   Found " + products.size() + " product(s):");
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            System.out.printf("   %d. ID: %d | Name: %-20s | Price: %8.2f | Category: %s%n",
                    i + 1,
                    p.getId(),
                    p.getName(),
                    p.getPrice(),
                    p.getCategoryName());
        }
    }
}