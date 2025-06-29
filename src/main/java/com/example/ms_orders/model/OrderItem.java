package com.example.ms_orders.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Cantidad del producto en la orden
    private Integer quantity;

    // Referencia al producto: SOLO el ID (NO entidad Product)
    private Long productId;

    // (Opcional, pero recomendable) Copia el nombre del producto para histórico
    private String productName;

    // (Opcional) Copia el precio del producto en el momento de la compra
    private BigDecimal productPrice;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;
}
