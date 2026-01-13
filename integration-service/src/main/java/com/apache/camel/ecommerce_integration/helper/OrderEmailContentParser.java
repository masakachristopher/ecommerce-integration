package com.apache.camel.ecommerce_integration.helper;

import java.util.*;
import java.util.regex.*;

public class OrderEmailContentParser {

    // --- Extract customer ---
    public static String extractCustomer(String body) {
        Pattern pattern = Pattern.compile("Customer:\\s*(.+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(body);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }

    // --- Extract items ---
    public static List<Map<String, Object>> extractItems(String body) {
        Pattern itemPattern = Pattern.compile("-\\s*(SKU[- ]?\\d+)\\s*(x|qty|quantity)?\\s*(\\d+)?", Pattern.CASE_INSENSITIVE);
        Matcher matcher = itemPattern.matcher(body);

        List<Map<String, Object>> items = new ArrayList<>();
        while (matcher.find()) {
            String sku = matcher.group(1).trim();
            int quantity = 1; // default
            if (matcher.group(3) != null) {
                quantity = Integer.parseInt(matcher.group(3));
            }
            Map<String, Object> item = new HashMap<>();
            item.put("sku", sku);
            item.put("quantity", quantity);
            items.add(item);
        }
        return items;
    }

    // --- Extract shipping ---
    public static Map<String, String> extractShipping(String body) {
        Pattern pattern = Pattern.compile("Ship To:\\s*(.+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(body);

        Map<String, String> shipping = new HashMap<>();
        if (matcher.find()) {
            String[] parts = matcher.group(1).split(",");
            shipping.put("city", parts.length > 0 ? parts[0].trim() : "");
            shipping.put("country", parts.length > 1 ? parts[1].trim() : "");
        }
        return shipping;
    }

    // --- Extract payment ---
    public static Map<String, String> extractPayment(String body) {
        Pattern pattern = Pattern.compile("Payment:\\s*(.+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(body);

        Map<String, String> payment = new HashMap<>();
        if (matcher.find()) {
            String value = matcher.group(1).trim();
            if (value.toLowerCase().contains("invoice")) {
                payment.put("type", "INVOICE");
            } else if (value.toLowerCase().contains("card")) {
                payment.put("type", "CARD");
            } else {
                payment.put("type", "OTHER");
            }

            Pattern termsPattern = Pattern.compile("(NET\\s*\\d+)", Pattern.CASE_INSENSITIVE);
            Matcher termsMatcher = termsPattern.matcher(value);
            payment.put("terms", termsMatcher.find() ? termsMatcher.group(1).trim().replaceAll("\\s+", "_") : "UNKNOWN");
        }
        return payment;
    }

    // --- Extract currency ---
    public static String extractCurrency(String body) {
        Pattern pattern = Pattern.compile("Currency:\\s*(\\w+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(body);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }
}
