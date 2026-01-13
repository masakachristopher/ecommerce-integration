package com.ecommerce.order.service.impl;

import com.ecommerce.order.dto.PriceQuote;
import com.ecommerce.order.model.Product;
import com.ecommerce.order.service.FxService;
import com.ecommerce.order.service.PricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class PricingServiceImpl implements PricingService {
    private final FxService fxService;

    @Value("${app.configs.currency.base}")
    private String baseCurrency;

    public boolean isPreferredCustomer(String customerRef) {
        return customerRef != null && customerRef.startsWith("VIP");
    }

    public PriceQuote quote(Product product, String customerRef, String targetCurrency) {

        BigDecimal finalPrice = product.getBasePrice();
        BigDecimal discount = BigDecimal.ZERO;
        BigDecimal fxRate = BigDecimal.ONE;

        if (isPreferredCustomer(customerRef)) {
            discount = BigDecimal.valueOf(0.05);
            finalPrice = finalPrice.multiply(BigDecimal.ONE.subtract(discount));
        }

        if (!baseCurrency.equalsIgnoreCase(targetCurrency)) {
            fxRate = fxService.getRate(baseCurrency, targetCurrency);
            finalPrice = finalPrice.multiply(fxRate);
        }

        return new PriceQuote(
                finalPrice.setScale(2, RoundingMode.HALF_UP),
                targetCurrency,
                discount,
                fxRate,
                "RULE_V1"
        );
    }
}

