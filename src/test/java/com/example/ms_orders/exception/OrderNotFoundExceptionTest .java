package com.example.ms_orders.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrderNotFoundExceptionTest {

    @Test
    void shouldContainCorrectMessage() {
        Long id = 5L;
        OrderNotFoundException exception = new OrderNotFoundException(id);
        assertEquals("Orden no encontrada con ID: " + id, exception.getMessage());
    }
}
