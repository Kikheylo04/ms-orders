package com.example.ms_orders.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Producto no encontrado con ID: " + id);
    }
}
