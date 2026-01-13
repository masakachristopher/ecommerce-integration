package com.apache.camel.ecommerce_integration.dto;

// Inner class representing Kafka payload
public  record OrderCreatedPayload(
        String orderNumber,
        String customerEmail,
        String customerName,
        String customerId,
        String currency,
        String totalAmount
) {}