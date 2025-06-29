package com.example.ms_orders.exception;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ErrorDetailsTest {

    @Test
    void testFullConstructor() {
        LocalDateTime now = LocalDateTime.now();
        ErrorDetails error = new ErrorDetails(now, "Error message", "Error details", "404");

        assertEquals(now, error.getTimestamp());
        assertEquals("Error message", error.getMessage());
        assertEquals("Error details", error.getDetails());
        assertEquals("404", error.getErrorCode());
    }

    @Test
    void testConstructorWithoutErrorCode() {
        LocalDateTime now = LocalDateTime.now();
        ErrorDetails error = new ErrorDetails(now, "Error message", "Error details");

        assertEquals(now, error.getTimestamp());
        assertEquals("Error message", error.getMessage());
        assertEquals("Error details", error.getDetails());
        assertNull(error.getErrorCode());
    }
}
