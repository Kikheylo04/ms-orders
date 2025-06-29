package com.example.ms_orders.repository;


import com.example.ms_orders.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByClientContainingIgnoreCaseOrStatusContainingIgnoreCase(String client, String status, Pageable pageable);
}
