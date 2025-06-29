package com.example.ms_orders.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InvalidProductExceptionTest {

    @Test
    void shouldContainCorrectMessage() {
        InvalidProductException exception = new InvalidProductException();
        assertEquals("El producto es obligatorio y debe tener un ID", exception.getMessage());
    }
}
