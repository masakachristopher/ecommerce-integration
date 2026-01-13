package com.ecommerce.order.service;

import java.math.BigDecimal;

public interface FxService {
    BigDecimal getRate(String fromCurrency, String toCurrency);
}
