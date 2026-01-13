package com.ecommerce.order.dto;

import java.math.BigDecimal;
import java.util.List;

public record CreateOrderRequest(
        String customerId,
        List<OrderItemRequest> items,
        BigDecimal totalAmount
) {}