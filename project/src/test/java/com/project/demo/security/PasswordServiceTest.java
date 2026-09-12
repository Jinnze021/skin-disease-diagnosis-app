package com.project.demo.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordServiceTest {

    private PasswordService passwordService;

    @BeforeEach
    void setUp() {
        passwordService = new PasswordService();
    }

    @Test
    void hashesAndMatchesWithoutStoringPlaintext() {
        String hash = passwordService.hash("SafePass123!");

        assertNotEquals("SafePass123!", hash);
        assertTrue(passwordService.matches("SafePass123!", hash));
        assertFalse(passwordService.matches("wrong-password", hash));
    }

    @Test
    void rejectsNullBlankAndShortPasswords() {
        assertThrows(IllegalArgumentException.class, () -> passwordService.hash(null));
        assertThrows(IllegalArgumentException.class, () -> passwordService.hash("        "));
        assertThrows(IllegalArgumentException.class, () -> passwordService.hash("short7"));
    }

    @Test
    void rejectsPasswordsLongerThanSeventyTwoUtf8Bytes() {
        String seventyFiveBytes = String.join("", Collections.nCopies(25, "皮"));

        assertThrows(IllegalArgumentException.class, () -> passwordService.hash(seventyFiveBytes));
    }

    @Test
    void rejectsInvalidLoginInputsWithoutThrowing() {
        assertFalse(passwordService.matches(null, "$2a$12$invalid"));
        assertFalse(passwordService.matches("short", "$2a$12$invalid"));
        assertFalse(passwordService.matches("SafePass123!", "not-a-bcrypt-hash"));
    }
}
