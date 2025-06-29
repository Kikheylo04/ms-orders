package com.example.ms_orders.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long id) {
        super("Orden no encontrada con ID: " + id);
    }
}
