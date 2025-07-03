package com.example.ms_orders.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.example.ms_orders.model.Order;

import lombok.Data;

@Data
public class OrderDto {
    private Long id;
    private String client;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status;
    private List<OrderItemDto> items;
    private BigDecimal total;

    public OrderDto(Order order, List<OrderItemDto> items) {
        this.id = order.getId();
        this.client = order.getClient();
        this.createdAt = order.getCreatedAt();
        this.updatedAt = order.getUpdatedAt();
        this.status = order.getStatus();
        this.items = items;
        this.total = order.getTotal();
    }
}