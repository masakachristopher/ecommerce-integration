package com.ecommerce.order.dto;

import java.math.BigDecimal;
import java.util.List;


public record OrderDto(
        String orderNumber,
        String customerId,
        BigDecimal totalAmount,
        String status,
        List<OrderItemDto> items
){}

