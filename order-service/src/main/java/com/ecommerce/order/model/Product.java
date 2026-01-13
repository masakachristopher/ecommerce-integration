package com.ecommerce.order.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Data
public class Product {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String sku;

    private String name;

    private BigDecimal basePrice;
}
