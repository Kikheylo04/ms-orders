package com.example.ms_orders.dto;

import com.example.ms_orders.model.OrderItem;
import lombok.Data;


@Data
public class OrderItemDto {
    private Long id;
    private Integer quantity;
    private ProductDto product;

    public OrderItemDto(OrderItem item, ProductDto product) {
        this.id = item.getId();
        this.quantity = item.getQuantity();
        this.product = product;
    }
}
