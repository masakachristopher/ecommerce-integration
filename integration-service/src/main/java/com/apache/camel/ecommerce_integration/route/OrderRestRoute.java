package com.apache.camel.ecommerce_integration.route;

import com.apache.camel.ecommerce_integration.dto.OrderDto;
import com.apache.camel.ecommerce_integration.model.IntegrationAudit;
import com.apache.camel.ecommerce_integration.processor.OrderAuditProcessor;
import com.apache.camel.ecommerce_integration.processor.OrderEnrichProcessor;
import com.apache.camel.ecommerce_integration.processor.OrderValidatorProcessor;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderRestRoute extends RouteBuilder {

    private final OrderValidatorProcessor validator;
    private final OrderEnrichProcessor enricher;
    private final OrderAuditProcessor auditor;

    @Override
    public void configure() throws Exception {
        // REST API endpoint for order submission
        // from("rest:post:/orders?componentName=platform-http")
        from("rest:post:/orders")
                .routeId("order-rest-route")
                .process(exchange -> {
                    String originalPayload = exchange.getIn().getBody(String.class);
                    exchange.setProperty("originalPayload", originalPayload);
                    exchange.setProperty("source", IntegrationAudit.OrderSource.REST);
                })
                .unmarshal().json(JsonLibrary.Jackson, OrderDto.class)
                .process(validator)
                .process(enricher)
                .marshal().json(JsonLibrary.Jackson)
                .process(auditor)
                .log("Sending message to OrderProcessingRoute...")
                .to("direct:sendCommands");
    }
}
