package com.apache.camel.ecommerce_integration.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

@Component
public class NumberFormatter {

    public String formatAmount(BigDecimal amount, String currency) {
        if (amount == null) return "0.00";
        DecimalFormat df = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.US));
        return df.format(amount) + " " + currency;
    }
}
