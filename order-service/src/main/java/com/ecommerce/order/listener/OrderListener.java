package com.ecommerce.order.listener;

import com.ecommerce.order.dto.CreateOrderRequest;
import com.ecommerce.order.dto.Order;
import com.ecommerce.order.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
public class OrderListener {

    private final OrderService orderService;

    @JmsListener(destination = "order.create.command")
    public void receive(String json) throws JsonProcessingException {
        System.out.println("Received order from queue:: " + json);
        ObjectMapper mapper = new ObjectMapper();
        Order request = mapper.readValue(json, Order.class);
        orderService.create(request);
    }
}
