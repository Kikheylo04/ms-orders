package com.example.ms_orders.controller;

import com.example.ms_orders.dto.OrderDto;
import com.example.ms_orders.model.Order;
import com.example.ms_orders.service.OrderService;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Order create(@RequestBody Order order) {
        return orderService.create(order);
    }

    @PutMapping("/{id}")
    public Order update(@PathVariable Long id, @RequestBody Order order) {
        return orderService.update(id, order);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        orderService.delete(id);
    }

    @GetMapping("/{id}")
    public Order getById(@PathVariable Long id) {
        return orderService.getById(id);
    }

    @GetMapping
    public ResponseEntity<List<Order>> list(
            @RequestParam(defaultValue = "") String client,
            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Order> pageResult = orderService.list(client, status, pageable);
        return ResponseEntity.ok(pageResult.getContent());
    }
    @GetMapping("/{id}/with-product-details")
    public ResponseEntity<OrderDto> getOrderWithProductDetails(@PathVariable Long id) {
        OrderDto orderDto = orderService.getOrderWithProductDetails(id);
        return ResponseEntity.ok(orderDto);
    }
}
