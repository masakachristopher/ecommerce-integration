package com.ecommerce.order.dto;

import java.math.BigDecimal;

public record OrderItemRequest(
        String productId,
        Integer quantity,
        BigDecimal unitPrice
) {}