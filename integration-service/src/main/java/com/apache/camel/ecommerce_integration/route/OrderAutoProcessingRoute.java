package com.apache.camel.ecommerce_integration.route;

import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderAutoProcessingRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:autoOrder")
        .routeId("order-auto-order")
        .choice()
            .when(simple("${header.totalItems} > 1000"))
            .to("activemq:queue:order.high-value-approval.command?exchangePattern=InOnly")
        .otherwise()
            .to("activemq:queue:order.create.command?exchangePattern=InOnly")
        .log("Successfully process order automatically: ${body}");

    }
}
