package com.example.ms_orders.service;

import com.example.ms_orders.client.ProductClient;
import com.example.ms_orders.dto.OrderDto;
import com.example.ms_orders.dto.ProductDto;
import com.example.ms_orders.exception.OrderNotFoundException;
import com.example.ms_orders.exception.ProductNotFoundException;
import com.example.ms_orders.model.Order;
import com.example.ms_orders.model.OrderItem;
import com.example.ms_orders.repository.OrderRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductClient productClient;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrder_Success() {
        OrderItem item = new OrderItem();
        item.setProductId(1L);
        item.setQuantity(2);

        Order order = new Order();
        order.setItems(List.of(item));

        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("Café");
        productDto.setPrice(BigDecimal.TEN);

        when(productClient.getProductById(1L)).thenReturn(productDto);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order result = orderService.create(order);

        assertNotNull(result);
        assertEquals("Café", result.getItems().get(0).getProductName());
        verify(orderRepository).save(any(Order.class));
        verify(productClient).getProductById(1L);
    }

    @Test
    void testCreateOrder_ProductNotFound_ThrowsException() {
        OrderItem item = new OrderItem();
        item.setProductId(999L);
        item.setQuantity(1);

        Order order = new Order();
        order.setItems(List.of(item));

        when(productClient.getProductById(999L)).thenReturn(null);

        assertThrows(ProductNotFoundException.class, () -> orderService.create(order));
    }

    @Test
    void testCreateOrder_NullProductId_ThrowsInvalidParameterException() {
        OrderItem item = new OrderItem();
        item.setProductId(null);

        Order order = new Order();
        order.setItems(List.of(item));

        assertThrows(java.security.InvalidParameterException.class, () -> orderService.create(order));
    }

    @Test
    void testUpdateOrder_Success() {
        Long orderId = 1L;

        OrderItem item = new OrderItem();
        item.setProductId(10L);
        item.setQuantity(2);

        Order updatedOrder = new Order();
        updatedOrder.setItems(List.of(item));

        Order existingOrder = new Order();
        existingOrder.setId(orderId);
        existingOrder.setItems(new ArrayList<>());

        ProductDto productDto = new ProductDto();
        productDto.setId(10L);
        productDto.setName("Café");
        productDto.setPrice(BigDecimal.TEN);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(productClient.getProductById(10L)).thenReturn(productDto);
        when(orderRepository.save(any(Order.class))).thenReturn(existingOrder);

        Order result = orderService.update(orderId, updatedOrder);

        assertNotNull(result);
        assertEquals("Café", result.getItems().get(0).getProductName());
        verify(orderRepository).save(existingOrder);
        verify(productClient).getProductById(10L);
    }

    @Test
    void testUpdateOrder_OrderNotFound_ThrowsException() {
        Long orderId = 99L;
        Order updatedOrder = new Order();
        updatedOrder.setItems(List.of());

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.update(orderId, updatedOrder));
    }

    @Test
    void testUpdateOrder_ProductNotFound_ThrowsException() {
        Long orderId = 1L;

        OrderItem item = new OrderItem();
        item.setProductId(55L);

        Order updatedOrder = new Order();
        updatedOrder.setItems(List.of(item));

        Order existingOrder = new Order();
        existingOrder.setId(orderId);
        existingOrder.setItems(new ArrayList<>());

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(productClient.getProductById(55L)).thenReturn(null);

        assertThrows(ProductNotFoundException.class, () -> orderService.update(orderId, updatedOrder));
    }

    @Test
    void testUpdateOrder_NullProductId_ThrowsInvalidParameterException() {
        Long orderId = 1L;

        OrderItem item = new OrderItem();
        item.setProductId(null);

        Order updatedOrder = new Order();
        updatedOrder.setItems(List.of(item));

        Order existingOrder = new Order();
        existingOrder.setId(orderId);
        existingOrder.setItems(new ArrayList<>());

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));

        assertThrows(java.security.InvalidParameterException.class, () -> orderService.update(orderId, updatedOrder));
    }

    @Test
    void testDeleteOrder_Success() {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        orderService.delete(orderId);

        verify(orderRepository).delete(order);
    }

    @Test
    void testDeleteOrder_OrderNotFound_ThrowsException() {
        Long orderId = 100L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.delete(orderId));
    }

    @Test
    void testGetById_Success() {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Order result = orderService.getById(orderId);

        assertNotNull(result);
        assertEquals(orderId, result.getId());
        verify(orderRepository).findById(orderId);
    }

    @Test
    void testGetById_OrderNotFound_ThrowsException() {
        Long orderId = 99L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.getById(orderId));
    }

    @Test
    void testGetOrderWithProductDetails_Success() {
        Long orderId = 1L;

        OrderItem item = new OrderItem();
        item.setProductId(42L);
        item.setQuantity(2);

        Order order = new Order();
        order.setId(orderId);
        order.setItems(List.of(item));

        ProductDto productDto = new ProductDto();
        productDto.setId(42L);
        productDto.setName("Producto Test");
        productDto.setPrice(BigDecimal.valueOf(5));

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(productClient.getProductById(42L)).thenReturn(productDto);

        OrderDto result = orderService.getOrderWithProductDetails(orderId);

        assertNotNull(result);
        assertEquals(orderId, result.getId());
        assertEquals("Producto Test", result.getItems().get(0).getProduct().getName());
        verify(orderRepository).findById(orderId);
        verify(productClient).getProductById(42L);
    }

    @Test
    void testGetOrderWithProductDetails_OrderNotFound_ThrowsException() {
        Long orderId = 999L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderWithProductDetails(orderId));
    }

    @Test
void testListOrders_ReturnsPage() {
    Pageable pageable = mock(Pageable.class);
    Page<Order> orderPage = mock(Page.class);

    when(orderRepository.findAll(pageable)).thenReturn(orderPage);

    Page<Order> result = orderService.list("cliente", "activo", pageable);

    assertNotNull(result);
    verify(orderRepository).findAll(pageable);
}

}
