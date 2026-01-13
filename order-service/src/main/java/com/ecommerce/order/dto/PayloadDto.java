package com.ecommerce.order.dto;

import java.math.BigDecimal;
import java.util.List;

public record PayloadDto (
        String orderNumber,
        String customerId,
        BigDecimal totalAmount,
        String status,
        String currency,
        String customerEmail,
        String externalOrderNumber,
        List<com.ecommerce.order.model.Order.OrderItem> items
){}

