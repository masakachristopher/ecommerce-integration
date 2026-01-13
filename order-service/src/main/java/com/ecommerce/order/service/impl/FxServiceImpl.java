package com.ecommerce.order.service.impl;

import com.ecommerce.order.service.FxService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FxServiceImpl implements FxService {

    // can load from config or DB
    private static final Map<String, BigDecimal> FX_RATES = Map.of(
            "USD_TZS", BigDecimal.valueOf(2500),
            "USD_USD", BigDecimal.ONE,
            "TZS_USD", BigDecimal.valueOf(1).divide(BigDecimal.valueOf(2500), 6, RoundingMode.HALF_UP),
            "TZS_TZS", BigDecimal.ONE
    );

    public BigDecimal getRate(String fromCurrency, String toCurrency) {
        if (fromCurrency.equalsIgnoreCase(toCurrency)) {
            return BigDecimal.ONE;
        }
        String key = fromCurrency.toUpperCase() + "_" + toCurrency.toUpperCase();
        BigDecimal rate = FX_RATES.get(key);
        if (rate == null) {
            throw new IllegalArgumentException("No FX rate available for " + key);
        }
        return rate;
    }
}

