package com.productmanagement.dao;

import com.productmanagement.model.Category;
import com.productmanagement.model.Product;
import org.junit.jupiter.api.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DataRetrieverTest {
    private DataRetriever dataRetriever;
    private Instant feb1;
    private Instant mar1;

    @BeforeAll
    void setUp() {
        dataRetriever = new DataRetriever();

        feb1 = LocalDateTime.of(2024, 2, 1, 0, 0, 0)
                .atZone(ZoneId.systemDefault()).toInstant();
        mar1 = LocalDateTime.of(2024, 3, 1, 0, 0, 0)
                .atZone(ZoneId.systemDefault()).toInstant();
    }

    @BeforeEach
    void printTestStart(TestInfo testInfo) {
        System.out.println("Starting test: " + testInfo.getDisplayName());
    }


    @Test
    @DisplayName("Test 1: getAllCategories should return all categories")
    void testGetAllCategories_ShouldReturnCategories() {
        List<Category> categories = dataRetriever.getAllCategories();

        assertNotNull(categories, "Categories list should not be null");
        assertFalse(categories.isEmpty(), "Categories list should not be empty");

        assertEquals(7, categories.size(), "Should have 7 categories from database");
    }

    @Test
    @DisplayName("Test 2: getAllCategories should contain specific categories")
    void testGetAllCategories_ShouldContainSpecificCategories() {
        List<Category> categories = dataRetriever.getAllCategories();

        boolean hasInformatique = categories.stream()
                .anyMatch(c -> c.getName().equalsIgnoreCase("informatique"));
        boolean hasAudio = categories.stream()
                .anyMatch(c -> c.getName().equalsIgnoreCase("audio"));

        assertTrue(hasInformatique, "Should contain 'Informatique' category");
        assertTrue(hasAudio, "Should contain 'Audio' category");
    }

    @Test
    @DisplayName("Test 3: getAllCategories categories should have IDs")
    void testGetAllCategories_ShouldHaveIds() {
        List<Category> categories = dataRetriever.getAllCategories();

        for (Category category : categories) {
            assertTrue(category.getId() > 0, "Category should have positive ID");
            assertNotNull(category.getName(), "Category name should not be null");
            assertFalse(category.getName().trim().isEmpty(), "Category name should not be empty");
        }
    }


    @Test
    @DisplayName("Test 4: getProductList page 1 size 2 should return first 2 products")
    void testGetProductList_Page1Size2_ShouldReturnFirstTwoProducts() {
        List<Product> products = dataRetriever.getProductList(1, 2);

        assertEquals(2, products.size(), "Should return exactly 2 products");
        assertEquals(1, products.get(0).getId(), "First product should be ID 1");
        assertEquals(2, products.get(1).getId(), "Second product should be ID 2");
    }

    @Test
    @DisplayName("Test 5: getProductList page 2 size 2 should return next 2 products")
    void testGetProductList_Page2Size2_ShouldReturnNextTwoProducts() {
        List<Product> products = dataRetriever.getProductList(2, 2);

        assertEquals(2, products.size(), "Should return exactly 2 products");
        assertEquals(3, products.get(0).getId(), "First product should be ID 3");
        assertEquals(4, products.get(1).getId(), "Second product should be ID 4");
    }

    @Test
    @DisplayName("Test 6: getProductList page 3 size 2 should return last product")
    void testGetProductList_Page3Size2_ShouldReturnLastProduct() {
        List<Product> products = dataRetriever.getProductList(3, 2);

        assertEquals(1, products.size(), "Should return 1 product (the last one)");
        assertEquals(5, products.getFirst().getId(), "Should be product ID 5");
    }

    @Test
    @DisplayName("Test 7: getProductList page 10 size 5 should return empty list")
    void testGetProductList_PageBeyondData_ShouldReturnEmpty() {
        List<Product> products = dataRetriever.getProductList(10, 5);

        assertNotNull(products, "Should return empty list, not null");
        assertTrue(products.isEmpty(), "Should return empty list when page beyond data");
    }

    @Test
    @DisplayName("Test 8: getProductList should have all product properties populated")
    void testGetProductList_ShouldHaveCompleteProductData() {
        List<Product> products = dataRetriever.getProductList(1, 1);

        if (!products.isEmpty()) {
            Product product = products.getFirst();
            assertTrue(product.getId() > 0, "Product should have ID");
            assertNotNull(product.getName(), "Product should have name");
            assertTrue(product.getPrice() >= 0, "Product price should be non-negative");
            assertNotNull(product.getCategoryName(), "Product should have category name");
            assertNotNull(product.getCreationDateTime(), "Product should have creation date");
        }
    }


    @Test
    @DisplayName("Test 9: Filter by product name 'dell' (case-insensitive)")
    void testFilterByProductName_ShouldFindDellProducts() {
        List<Product> products = dataRetriever.getProductsByCriteria("dell", null, null, null);

        assertEquals(1, products.size(), "Should find 1 product with 'dell' in name");
        assertEquals("Laptop Dell XPS", products.getFirst().getName());
        assertTrue(products.getFirst().getCategoryName().toLowerCase().contains("informatique"));
    }

    @Test
    @DisplayName("Test 10: Filter by category 'info' (partial match)")
    void testFilterByCategoryInfo_ShouldFindInformatiqueProducts() {
        List<Product> products = dataRetriever.getProductsByCriteria(null, "info", null, null);

        assertEquals(2, products.size(), "Should find 2 products in 'informatique' category");

        boolean foundLaptop = products.stream()
                .anyMatch(p -> p.getName().equals("Laptop Dell XPS"));
        boolean foundEcran = products.stream()
                .anyMatch(p -> p.getName().contains("Samsung"));

        assertTrue(foundLaptop, "Should include Laptop Dell XPS");
        assertTrue(foundEcran, "Should include Ecran Samsung");
    }

    @Test
    @DisplayName("Test 11: Filter by product name and category")
    void testFilterByProductNameAndCategory_ShouldFindiPhoneMobile() {
        List<Product> products = dataRetriever.getProductsByCriteria("iphone", "mobile", null, null);

        assertEquals(1, products.size(), "Should find iPhone in mobile category");
        assertTrue(products.getFirst().getName().toLowerCase().contains("iphone"));
        assertTrue(products.getFirst().getCategoryName().toLowerCase().contains("mobile"));
    }

    @Test
    @DisplayName("Test 12: Filter by date range (February 2024)")
    void testFilterByDateRange_ShouldFindFebruaryProducts() {
        Instant startFeb = feb1;
        Instant endFeb = LocalDateTime.of(2024, 2, 29, 23, 59, 59)
                .atZone(ZoneId.systemDefault()).toInstant();

        List<Product> products = dataRetriever.getProductsByCriteria(null, null, startFeb, endFeb);

        assertEquals(2, products.size(), "Should find 2 products created in February");

        for (Product product : products) {
            Instant created = product.getCreationDateTime();
            assertTrue(created.isAfter(startFeb) || created.equals(startFeb),
                    "Product should be created on or after February 1");
            assertTrue(created.isBefore(endFeb) || created.equals(endFeb),
                    "Product should be created on or before February 29");
        }
    }

    @Test
    @DisplayName("Test 13: Filter by 'samsung' and 'bureau' category")
    void testFilterBySamsungAndBureau_ShouldFindSamsungMonitor() {
        List<Product> products = dataRetriever.getProductsByCriteria("samsung", "bureau", null, null);

        assertEquals(1, products.size(), "Should find Samsung monitor with bureau category");
        assertTrue(products.getFirst().getName().toLowerCase().contains("samsung"));
        assertTrue(products.getFirst().getCategoryName().toLowerCase().contains("bureau"));
    }

    @Test
    @DisplayName("Test 14: Filter with all null parameters should return all products")
    void testFilterWithAllNull_ShouldReturnAllProducts() {
        List<Product> products = dataRetriever.getProductsByCriteria(null, null, null, null);

        assertEquals(5, products.size(), "Should return all 5 products when no filters");
    }


    @Test
    @DisplayName("Test 15: Filter with pagination - no filters, page 1 size 10")
    void testFilterWithPagination_NoFiltersPage1Size10() {
        List<Product> products = dataRetriever.getProductsByCriteria(null, null, null, null, 1, 10);

        assertEquals(5, products.size(), "Should return all 5 products");
    }

    @Test
    @DisplayName("Test 16: Filter with pagination - search 'del', page 1 size 5")
    void testFilterWithPagination_SearchDelPage1Size5() {
        List<Product> products = dataRetriever.getProductsByCriteria("del", null, null, null, 1, 5);

        assertEquals(1, products.size(), "Should find 1 product with 'del'");
        assertTrue(products.getFirst().getName().toLowerCase().contains("dell"));
    }

    @Test
    @DisplayName("Test 17: Filter with pagination - category 'informatique', page 1 size 10")
    void testFilterWithPagination_CategoryInformatiquePage1Size10() {
        List<Product> products = dataRetriever.getProductsByCriteria(null, "informatique", null, null, 1, 10);

        assertEquals(2, products.size(), "Should find 2 products in informatique category");
    }

    @Test
    @DisplayName("Test 18: Filter with pagination - page 1 size 2 should return first 2 matches")
    void testFilterWithPagination_Page1Size2_ReturnsFirstTwoMatches() {
        List<Product> products = dataRetriever.getProductsByCriteria(null, null, null, null, 1, 2);

        assertEquals(2, products.size(), "Should return exactly 2 products");
        assertEquals(1, products.get(0).getId(), "First should be ID 1");
        assertEquals(2, products.get(1).getId(), "Second should be ID 2");
    }

    @Test
    @DisplayName("Test 19: Filter with pagination - page 2 size 2 should return next 2 matches")
    void testFilterWithPagination_Page2Size2_ReturnsNextTwoMatches() {
        List<Product> products = dataRetriever.getProductsByCriteria(null, null, null, null, 2, 2);

        assertEquals(2, products.size(), "Should return exactly 2 products");
        assertEquals(3, products.get(0).getId(), "First should be ID 3");
        assertEquals(4, products.get(1).getId(), "Second should be ID 4");
    }

    @Test
    @DisplayName("Test 20: Empty search should return empty list")
    void testFilter_EmptySearch_ReturnsEmpty() {
        List<Product> products = dataRetriever.getProductsByCriteria("nonexistent123", null, null, null);

        assertNotNull(products, "Should return empty list, not null");
        assertTrue(products.isEmpty(), "Should return empty list for non-existent product");
    }

    @AfterEach
    void printTestEnd(TestInfo testInfo) {
        System.out.println("Finished test: " + testInfo.getDisplayName());
        System.out.println("---");
    }

    @AfterAll
    static void tearDown() {
        System.out.println("All tests completed!");
    }
}