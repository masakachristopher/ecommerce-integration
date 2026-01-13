package com.ecommerce.order.service;

import com.ecommerce.order.model.Order;

public interface OrderService {
    Order create(com.ecommerce.order.dto.Order request);
    void markInventoryReserved(Long orderId);
}