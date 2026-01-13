package com.ecommerce.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class Order {
    private String externalOrderRef;
    private String customerRef;
    private List<OrderItem> items;
    private Shipping shipping;
    private Payment payment;
    private String currency;
}