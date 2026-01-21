package com.apache.camel.ecommerce_integration.processor;

import com.apache.camel.ecommerce_integration.dto.*;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CsvToOrderPayloadProcessor implements Processor {

    @Override
    public void process(Exchange exchange) {

        List<CsvOrder> rows = exchange.getIn().getBody(List.class);

        Map<String, Order> orders = new LinkedHashMap<>();

        for (CsvOrder row : rows) {

            Order order = orders.computeIfAbsent(row.getOrderRef(), ref -> {
                Order o = new Order();
                o.setExternalOrderRef(ref);

                o.setCustomerRef(row.getCustomer() != null ? row.getCustomer().trim() : "UNKNOWN_CUSTOMER");
                o.setCurrency(row.getCurrency() != null ? row.getCurrency() : "USD");
                o.setPayment(new Payment(
                        row.getPayment() != null ? row.getPayment() : "UNKNOWN",
                        row.getPaymentTerms() != null ? row.getPaymentTerms() : "UNKNOWN"
                ));
                o.setShipping(new Shipping(
                        row.getShipCountry() != null ? row.getShipCountry() : "",
                        row.getShipCity() != null ? row.getShipCity() : ""
                ));
                o.setItems(new ArrayList<>());
                return o;
            });

            // Items (same logic as email: quantity defaults to 1)
            int qty = row.getQuantity() != null ? row.getQuantity() : 1;
            order.getItems().add(new OrderItem(row.getSku(), qty));
        }

        exchange.getIn().setHeader("totalOrders", orders.size());
        exchange.getIn().setBody(new ArrayList<>(orders.values()));
    }
}
