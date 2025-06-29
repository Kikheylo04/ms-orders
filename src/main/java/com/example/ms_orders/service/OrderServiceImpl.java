package com.example.ms_orders.service;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.stream.Collectors;

import com.example.ms_orders.client.ProductClient;
import com.example.ms_orders.dto.OrderDto;
import com.example.ms_orders.dto.OrderItemDto;
import com.example.ms_orders.dto.ProductDto;
import com.example.ms_orders.exception.OrderNotFoundException;
import com.example.ms_orders.exception.ProductNotFoundException;
import com.example.ms_orders.model.Order;
import com.example.ms_orders.model.OrderItem;
import com.example.ms_orders.repository.OrderRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    public OrderServiceImpl(OrderRepository orderRepository, ProductClient productClient) {
        this.orderRepository = orderRepository;
        this.productClient = productClient;
    }

    @Override
    public Order create(Order order) {
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItem item : order.getItems()) {
            if (item.getProductId() == null) {
                throw new InvalidParameterException("productId es obligatorio");
            }

            ProductDto realProduct = productClient.getProductById(item.getProductId());
            if (realProduct == null) {
                throw new ProductNotFoundException(item.getProductId());
            }

            item.setProductName(realProduct.getName());
            item.setProductPrice(realProduct.getPrice());
            item.setOrder(order);

            BigDecimal subtotal = realProduct.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(subtotal);
        }

        order.setTotal(total);
        return orderRepository.save(order);
    }

    @Override
    public Order update(Long id, Order updatedOrder) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        existingOrder.setClient(updatedOrder.getClient());
        existingOrder.setDate(updatedOrder.getDate());
        existingOrder.setStatus(updatedOrder.getStatus());

        existingOrder.getItems().clear();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItem item : updatedOrder.getItems()) {
            if (item.getProductId() == null) {
                throw new InvalidParameterException("productId es obligatorio");
            }

            ProductDto realProduct = productClient.getProductById(item.getProductId());
            if (realProduct == null) {
                throw new ProductNotFoundException(item.getProductId());
            }

            item.setProductName(realProduct.getName());
            item.setProductPrice(realProduct.getPrice());
            item.setOrder(existingOrder);

            BigDecimal subtotal = realProduct.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(subtotal);

            existingOrder.getItems().add(item);
        }

        existingOrder.setTotal(total);
        return orderRepository.save(existingOrder);
    }

    @Override
    public void delete(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        orderRepository.delete(order);
    }

    @Override
    public Order getById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

     @Override
    public OrderDto getOrderWithProductDetails(Long id) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new OrderNotFoundException(id));

        List<OrderItemDto> itemDtos = order.getItems().stream()
            .map(item -> {
                ProductDto product = productClient.getProductById(item.getProductId());
                return new OrderItemDto(item, product);
            })
            .collect(Collectors.toList());

        return new OrderDto(order, itemDtos);

    }

    @Override
    public Page<Order> list(String client, String status, Pageable pageable) {
        return orderRepository.findAll(pageable);
    }
}
