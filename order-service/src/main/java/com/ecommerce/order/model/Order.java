package com.ecommerce.order.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "external_order_number")
    private String externalOrderNumber;

    @Column(name = "customer_id")
    private String customerId;

    private BigDecimal total;

    private String currency;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.CREATED;

    private LocalDateTime createdAt = LocalDateTime.now();

    @ElementCollection
    private List<OrderItem> items;

    public enum OrderStatus {
        CREATED, INVENTORY_RESERVED, SHIPPED, FAILED
    }

    @Embeddable
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class OrderItem {
        private String productId;
        private Integer quantity;
        private BigDecimal unitPrice;
    }
}
