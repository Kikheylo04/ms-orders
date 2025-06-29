package com.example.ms_orders.service;

import com.example.ms_orders.dto.OrderDto;
import com.example.ms_orders.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Order create(Order order);
    Order update(Long id, Order order);
    void delete(Long id);
    Order getById(Long id);
    Page<Order> list(String client, String status, Pageable pageable);
    OrderDto getOrderWithProductDetails(Long id);
}
