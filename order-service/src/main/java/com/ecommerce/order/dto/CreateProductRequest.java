package com.ecommerce.order.dto;

import java.math.BigDecimal;

public record CreateProductRequest(
        String sku,
        String name,
        String baseCurrency,
        BigDecimal basePrice
) {}
