package com.productmanagement;

import com.productmanagement.dao.DataRetriever;
import com.productmanagement.model.Category;
import com.productmanagement.model.Product;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== TESTING PRODUCT MANAGEMENT SYSTEM ===\n");

        DataRetriever retriever = new DataRetriever();

        // Test a) getAllCategories()
        System.out.println("a) Testing getAllCategories()");
        System.out.println("------------------------------");
        testGetAllCategories(retriever);

        // Test b) getProductList(page, size)
        System.out.println("\n\nb) Testing getProductList(page, size)");
        System.out.println("---------------------------------------");
        testGetProductList(retriever);

        // Test c) getProductsByCriteria(productName, categoryName, creationMin, creationMax)
        System.out.println("\n\nc) Testing getProductsByCriteria(productName, categoryName, creationMin, creationMax)");
        System.out.println("--------------------------------------------------------------------------------------");
        testGetProductsByCriteriaWithoutPagination(retriever);

        // Test d) getProductsByCriteria(productName, categoryName, creationMin, creationMax, page, size)
        System.out.println("\n\nd) Testing getProductsByCriteria(productName, categoryName, creationMin, creationMax, page, size)");
        System.out.println("--------------------------------------------------------------------------------------------------");
        testGetProductsByCriteriaWithPagination(retriever);
    }

    // ============ TEST a) getAllCategories() ============
    private static void testGetAllCategories(DataRetriever retriever) {
        List<Category> categories = retriever.getAllCategories();
        System.out.println("Number of categories: " + categories.size());
        for (Category category : categories) {
            System.out.println("  - ID: " + category.getId() + ", Name: " + category.getName());
        }
    }

    // ============ TEST b) getProductList(page, size) ============
    private static void testGetProductList(DataRetriever retriever) {
        // Test cases from the table
        int[][] testCases = {
                {1, 10},  // page=1, size=10
                {1, 5},   // page=1, size=5
                {1, 3},   // page=1, size=3
                {2, 2}    // page=2, size=2
        };

        for (int i = 0; i < testCases.length; i++) {
            int page = testCases[i][0];
            int size = testCases[i][1];

            System.out.println("\nTest case " + (i+1) + ": page=" + page + ", size=" + size);
            List<Product> products = retriever.getProductList(page, size);
            System.out.println("Number of products returned: " + products.size());

            for (Product product : products) {
                System.out.println("  - ID: " + product.getId() +
                        ", Name: " + product.getName() +
                        ", Price: " + product.getPrice() +
                        ", Categories: " + product.getCategoryName());
            }
        }
    }

    // ============ TEST c) getProductsByCriteria WITHOUT pagination ============
    private static void testGetProductsByCriteriaWithoutPagination(DataRetriever retriever) {
        // Create Instant for date tests
        Instant feb1 = LocalDateTime.of(2024, 2, 1, 0, 0, 0)
                .atZone(ZoneId.systemDefault()).toInstant();
        Instant mar1 = LocalDateTime.of(2024, 3, 1, 0, 0, 0)
                .atZone(ZoneId.systemDefault()).toInstant();
        Instant jan1 = LocalDateTime.of(2024, 1, 1, 0, 0, 0)
                .atZone(ZoneId.systemDefault()).toInstant();
        Instant dec1 = LocalDateTime.of(2024, 12, 1, 0, 0, 0)
                .atZone(ZoneId.systemDefault()).toInstant();

        // Test cases from the table
        Object[][] testCases = {
                // productName, categoryName, creationMin, creationMax
                {"Dell", null, null, null},
                {null, "Info", null, null},
                {"iPhone", "mobile", null, null},
                {null, null, feb1, mar1},
                {"Samsung", "bureau", null, null},
                {"Sony", "Informatione", null, null},
                {null, "audio", jan1, dec1},
                {null, null, null, null}
        };

        for (int i = 0; i < testCases.length; i++) {
            String productName = (String) testCases[i][0];
            String categoryName = (String) testCases[i][1];
            Instant creationMin = (Instant) testCases[i][2];
            Instant creationMax = (Instant) testCases[i][3];

            System.out.println("\nTest case " + (i+1) + ":");
            System.out.println("  productName: " + productName +
                    ", categoryName: " + categoryName +
                    ", creationMin: " + formatInstant(creationMin) +
                    ", creationMax: " + formatInstant(creationMax));

            List<Product> products = retriever.getProductsByCriteria(
                    productName, categoryName, creationMin, creationMax);

            System.out.println("  Number of products returned: " + products.size());

            for (Product product : products) {
                System.out.println("  - ID: " + product.getId() +
                        ", Name: " + product.getName() +
                        ", Price: " + product.getPrice() +
                        ", Categories: " + product.getCategoryName());
            }
        }
    }

    // ============ TEST d) getProductsByCriteria WITH pagination ============
    private static void testGetProductsByCriteriaWithPagination(DataRetriever retriever) {
        // Test cases from the table
        Object[][] testCases = {
                // productName, categoryName, creationMin, creationMax, page, size
                {null, null, null, null, 1, 10},
                {"Del", null, null, null, 1, 5},
                {null, "Informatique", null, null, 1, 10}
        };

        for (int i = 0; i < testCases.length; i++) {
            String productName = (String) testCases[i][0];
            String categoryName = (String) testCases[i][1];
            Instant creationMin = (Instant) testCases[i][2];
            Instant creationMax = (Instant) testCases[i][3];
            int page = (Integer) testCases[i][4];
            int size = (Integer) testCases[i][5];

            System.out.println("\nTest case " + (i+1) + ":");
            System.out.println("  productName: " + productName +
                    ", categoryName: " + categoryName +
                    ", creationMin: " + formatInstant(creationMin) +
                    ", creationMax: " + formatInstant(creationMax) +
                    ", page: " + page +
                    ", size: " + size);

            List<Product> products = retriever.getProductsByCriteria(
                    productName, categoryName, creationMin, creationMax, page, size);

            System.out.println("  Number of products returned: " + products.size());

            for (Product product : products) {
                System.out.println("  - ID: " + product.getId() +
                        ", Name: " + product.getName() +
                        ", Price: " + product.getPrice() +
                        ", Categories: " + product.getCategoryName());
            }
        }
    }

    // ============ HELPER METHOD ============
    private static String formatInstant(Instant instant) {
        if (instant == null) {
            return "null";
        }
        return instant.atZone(ZoneId.systemDefault()).toLocalDate().toString();
    }
}