package com.example.ms_orders.controller;

import com.example.ms_orders.dto.OrderDto;
import com.example.ms_orders.dto.OrderItemDto;
import com.example.ms_orders.model.Order;
import com.example.ms_orders.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void testCreateOrder() throws Exception {
        Order order = new Order();
        order.setClient("Cliente Test");

        when(orderService.create(any(Order.class))).thenReturn(order);

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.client").value("Cliente Test"));
    }

    @Test
    void testUpdateOrder() throws Exception {
        Order order = new Order();
        order.setId(1L);
        order.setClient("Cliente Actualizado");

        when(orderService.update(eq(1L), any(Order.class))).thenReturn(order);

        mockMvc.perform(put("/api/orders/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.client").value("Cliente Actualizado"));
    }

    @Test
    void testDeleteOrder() throws Exception {
        doNothing().when(orderService).delete(1L);

        mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetById() throws Exception {
        Order order = new Order();
        order.setId(1L);
        order.setClient("Cliente Uno");

        when(orderService.getById(1L)).thenReturn(order);

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.client").value("Cliente Uno"));
    }

    @Test
    void testListOrders() throws Exception {
        Order order1 = new Order();
        order1.setClient("Cliente A");

        Page<Order> page = new PageImpl<>(List.of(order1));
        when(orderService.list(anyString(), anyString(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/orders")
                .param("client", "")
                .param("status", "")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void testGetOrderWithProductDetails_Success() throws Exception {
        Long orderId = 1L;

        Order order = new Order();
        order.setId(orderId);
        order.setClient("Juan");
        order.setDate(LocalDate.now());
        order.setStatus("activo");
        order.setTotal(BigDecimal.TEN);

        List<OrderItemDto> items = new ArrayList<>();

        OrderDto orderDto = new OrderDto(order, items);

        when(orderService.getOrderWithProductDetails(orderId)).thenReturn(orderDto);

        mockMvc.perform(get("/api/orders/{id}/with-product-details", orderId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId))
                .andExpect(jsonPath("$.client").value("Juan"))
                .andExpect(jsonPath("$.status").value("activo"));

        verify(orderService).getOrderWithProductDetails(orderId);
    }

}
