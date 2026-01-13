package com.ecommerce.order.controller;

import com.ecommerce.order.constant.PathConstants;
import com.ecommerce.order.dto.CreateOrderRequest;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(PathConstants.API_V1_ORDER)
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody com.ecommerce.order.dto.Order request) {
        Order created = orderService.create(request);
        return ResponseEntity.ok(created);
    }

//    @PostMapping("/{orderId}/inventory-reserved")
//    public ResponseEntity<Void> markInventoryReserved(@PathVariable Long orderId) {
//        orderService.markInventoryReserved(orderId);
//        return ResponseEntity.ok().build();
//    }
}