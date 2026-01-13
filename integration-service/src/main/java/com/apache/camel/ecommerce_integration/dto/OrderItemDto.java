package com.apache.camel.ecommerce_integration.dto;

import java.math.BigDecimal;

public record OrderItemDto(
        String productId,
        Integer quantity,
        BigDecimal unitPrice
) {}
