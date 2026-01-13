package com.apache.camel.ecommerce_integration.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CustomerEnrich {

    public Map<String, Object> enrich(String customerId) {
        // In real-world, call DB or microservice
        Map<String, Object> customerData = new HashMap<>();
        customerData.put("name", "John Doe");
        customerData.put("address", "123 Main St");
        return customerData;
    }
}
