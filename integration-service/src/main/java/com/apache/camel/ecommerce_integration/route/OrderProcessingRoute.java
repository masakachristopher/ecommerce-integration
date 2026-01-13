package com.apache.camel.ecommerce_integration.route;

import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderProcessingRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // Global Error Handling
        errorHandler(deadLetterChannel("log:dead?level=ERROR")
                .maximumRedeliveries(3)
                .redeliveryDelay(1000));

        // Send Commands to ActiveMQ
        from("direct:sendCommands")
                .routeId("order-send-command")
                .to("activemq:queue:order.create.command?exchangePattern=InOnly")
                .setBody(constant("{\"status\":\"success\",\"message\":\"Order is being processed.\"}"));
    }
}
