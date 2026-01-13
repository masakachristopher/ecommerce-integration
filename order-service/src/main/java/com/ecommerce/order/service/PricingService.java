package com.ecommerce.order.service;

import com.ecommerce.order.dto.PriceQuote;
import com.ecommerce.order.model.Product;

public interface PricingService {
    boolean isPreferredCustomer(String customerRef);
    PriceQuote quote(Product product, String customerRef, String targetCurrency);
}

