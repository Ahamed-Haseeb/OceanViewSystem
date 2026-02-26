package com.oceanviewsystem.util;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;

public class DBConnectionTest {

    @Test
    public void testSingletonInstance() {
        // Fetch the instance twice
        DBConnection instance1 = DBConnection.getInstance();
        DBConnection instance2 = DBConnection.getInstance();

        // Assert that both variables point to the exact same memory instance (Singleton)
        assertSame(instance1, instance2, "DBConnection should be a Singleton (both instances must match)");
    }

    @Test
    public void testConnectionIsValid() {
        Connection conn = DBConnection.getInstance().getConnection();
        assertNotNull(conn, "Database connection should be successfully established and not null");
    }
}