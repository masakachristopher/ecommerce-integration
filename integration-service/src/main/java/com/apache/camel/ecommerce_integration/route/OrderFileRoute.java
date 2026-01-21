package com.apache.camel.ecommerce_integration.route;

import com.apache.camel.ecommerce_integration.dto.OrderDto;
import com.apache.camel.ecommerce_integration.model.IntegrationAudit;
import com.apache.camel.ecommerce_integration.processor.CsvToOrderPayloadProcessor;
import com.apache.camel.ecommerce_integration.processor.OrderAuditProcessor;
import com.apache.camel.ecommerce_integration.processor.OrderValidatorProcessor;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderFileRoute extends RouteBuilder {

    private final CsvToOrderPayloadProcessor csvProcessor;

    @Override
    public void configure() throws Exception {

        BindyCsvDataFormat csvFormat = new BindyCsvDataFormat(OrderDto.class);
        // Route to process order files from a directory
        from("file:incomingBatches?noop=true")
                .routeId("order-file-route")
                .process(exchange -> {
                    String originalPayload = exchange.getIn().getBody(String.class);
                    exchange.setProperty("originalPayload", originalPayload);
                    exchange.setProperty("source", IntegrationAudit.OrderSource.FILE);
                })
                .unmarshal(csvFormat)
                .process(csvProcessor)
                .split(body())
                .convertBodyTo(String.class)
                .to("direct:sendCommands");
    }
}