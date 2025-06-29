package com.example.ms_orders.exception;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

class GlobalExceptionHandlerTest {

    @Test
    void testHandleEntityNotFoundException() {
        EntityNotFoundException ex = new EntityNotFoundException("Recurso no encontrado");
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ResponseEntity<String> response = handler.handleNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Recurso no encontrado", response.getBody());
    }

    @Test
    void testHandleValidationException_ReturnsBadRequest() {

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(List.of());

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(
                mock(MethodParameter.class), bindingResult);

        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResponseEntity<String> response = handler.handleValidation(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Campos inválidos", response.getBody());
    }

}
