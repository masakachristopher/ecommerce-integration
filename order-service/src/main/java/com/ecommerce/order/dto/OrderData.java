package com.ecommerce.order.dto;

import com.ecommerce.order.model.Order;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderData {
    private Order order;
}
