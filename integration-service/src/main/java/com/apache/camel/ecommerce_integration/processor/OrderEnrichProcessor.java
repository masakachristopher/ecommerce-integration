package com.apache.camel.ecommerce_integration.processor;

import com.apache.camel.ecommerce_integration.dto.OrderDto;
import com.apache.camel.ecommerce_integration.service.CustomerEnrich;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class OrderEnrichProcessor implements Processor {

    private final CustomerEnrich customerEnrich;

    @Override
    public void process(Exchange exchange) {
        OrderDto order = exchange.getIn().getBody(OrderDto.class);
        Map<String, Object> customerData = customerEnrich.enrich(order.customerId());
//        order.setStatus("RECEIVED");
        exchange.getIn().setBody(order);
    }
}
