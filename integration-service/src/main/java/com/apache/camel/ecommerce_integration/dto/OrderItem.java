package com.apache.camel.ecommerce_integration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class OrderItem {
    private String sku;
    private int quantity;
}
