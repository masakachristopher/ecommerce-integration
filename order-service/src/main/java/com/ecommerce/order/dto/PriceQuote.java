package com.ecommerce.order.dto;

import java.math.BigDecimal;

public record PriceQuote (
    BigDecimal unitPrice,
    String currency,
    BigDecimal appliedDiscount,
    BigDecimal  exchangeRate,
    String priceSource
){}

