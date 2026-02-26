package com.oceanviewsystem.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testUserGettersAndSetters() {
        User user = new User();
        user.setId(1);
        user.setUsername("testuser");
        user.setPassword("hashedpassword123");
        user.setRole("STAFF");

        assertEquals(1, user.getId(), "User ID should match the set value");
        assertEquals("testuser", user.getUsername(), "Username should match the set value");
        assertEquals("hashedpassword123", user.getPassword(), "Password should match the set value");
        assertEquals("STAFF", user.getRole(), "Role should match the set value");
    }
}