package com.apache.camel.ecommerce_integration.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderDto (
        String customerId,
        List<OrderItemDto> items,
        BigDecimal totalAmount
) {}