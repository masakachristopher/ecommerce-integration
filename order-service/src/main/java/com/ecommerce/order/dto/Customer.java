package com.ecommerce.order.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Customer {
    private String customerId;
    private String name;
    private String email;
    private String phoneNumber;
}

