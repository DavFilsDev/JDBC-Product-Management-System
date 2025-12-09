package com.productmanagement;

import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DBConnectionTest {
    private DBConnection dbConnection;

    @BeforeAll
    void setUp() {
        dbConnection = new DBConnection();
    }

    @Test
    @DisplayName("Test 1: getDBConnection should return valid connection")
    void testGetDBConnection_ShouldReturnValidConnection() throws SQLException {
        Connection connection = dbConnection.getDBConnection();

        assertNotNull(connection, "Connection should not be null");
        assertFalse(connection.isClosed(), "Connection should be open");
        assertTrue(connection.isValid(2), "Connection should be valid");

        connection.close();
    }

    @Test
    @DisplayName("Test 2: Connection should have correct database metadata")
    void testConnectionMetadata() throws SQLException {
        try (Connection connection = dbConnection.getDBConnection()) {
            assertEquals("PostgreSQL", connection.getMetaData().getDatabaseProductName());
            assertTrue(connection.getMetaData().getURL().contains("product_management_db"));
        }
    }

    @Test
    @DisplayName("Test 3: Connection should support transactions")
    void testConnectionTransactionSupport() throws SQLException {
        try (Connection connection = dbConnection.getDBConnection()) {
            assertTrue(connection.getMetaData().supportsTransactions(),
                    "PostgreSQL should support transactions");

            boolean initialAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(!initialAutoCommit);
            assertEquals(!initialAutoCommit, connection.getAutoCommit(),
                    "Should be able to change auto-commit mode");
        }
    }
}