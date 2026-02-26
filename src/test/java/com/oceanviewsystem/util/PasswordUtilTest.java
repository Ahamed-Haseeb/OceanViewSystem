package com.oceanviewsystem.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordUtilTest {

    @Test
    public void testHashPasswordLength() {
        String plainText = "staff123";
        String hashed = PasswordUtil.hashPassword(plainText);

        // SHA-256 hash should always be 64 characters long
        assertNotNull(hashed, "Hashed password should not be null");
        assertEquals(64, hashed.length(), "SHA-256 hash length must be 64 characters");
    }

    @Test
    public void testHashPasswordConsistency() {
        String plainText = "admin123";
        String hash1 = PasswordUtil.hashPassword(plainText);
        String hash2 = PasswordUtil.hashPassword(plainText);

        // Same password should generate the exact same hash
        assertEquals(hash1, hash2, "Identical passwords must generate identical hashes");
    }
}