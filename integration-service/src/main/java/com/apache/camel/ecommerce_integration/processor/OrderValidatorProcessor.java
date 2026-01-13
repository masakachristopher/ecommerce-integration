package com.apache.camel.ecommerce_integration.processor;

import com.apache.camel.ecommerce_integration.dto.OrderDto;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OrderValidatorProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        OrderDto order = exchange.getIn().getBody(OrderDto.class);
        if (order == null || order.totalAmount() == null || order.totalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid order");
        }
    }
}
