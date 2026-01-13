package com.apache.camel.ecommerce_integration.processor;

import com.apache.camel.ecommerce_integration.dto.Order;
import com.apache.camel.ecommerce_integration.dto.OrderItem;
import com.apache.camel.ecommerce_integration.dto.Payment;
import com.apache.camel.ecommerce_integration.dto.Shipping;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
@RequiredArgsConstructor
public class EmailToOrderPayloadProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String body = exchange.getIn().getBody(String.class);
        String orderRef = exchange.getIn().getHeader("orderReference", String.class);

        Order order = new Order();
        order.setCustomerRef(extractCustomer(body));
        order.setItems(extractItems(body));
        order.setPayment(extractPayment(body));
        order.setShipping(extractShipping(body));
        order.setCurrency(extractCurrency(body));
        order.setExternalOrderRef(orderRef);

        int totalItems = extractItems(body).size();
        exchange.getIn().setHeader("totalItems", totalItems);
        exchange.getIn().setBody(order);
    }

    private String extractCustomer(String body) {
        Pattern pattern = Pattern.compile("Customer:\\s*(.+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(body);
        return matcher.find() ? matcher.group(1).trim() : "UNKNOWN_CUSTOMER";
    }

    private List<OrderItem> extractItems(String body) {
        Pattern itemPattern = Pattern.compile("-\\s*(SKU[- ]?\\d+)\\s*(x|qty|quantity)?\\s*(\\d+)?", Pattern.CASE_INSENSITIVE);
        Matcher matcher = itemPattern.matcher(body);

        List<OrderItem> items = new ArrayList<>();
        while (matcher.find()) {
            String sku = matcher.group(1).trim();
            int quantity = matcher.group(3) != null ? Integer.parseInt(matcher.group(3)) : 1;

            items.add(new OrderItem(sku, quantity));
        }
        return items;
    }

    private Shipping extractShipping(String body) {
        Pattern pattern = Pattern.compile("Ship To:\\s*(.+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(body);

        if (matcher.find()) {
            String[] parts = matcher.group(1).split(",");
            String city = parts.length > 0 ? parts[0].trim() : "";
            String country = parts.length > 1 ? parts[1].trim() : "";
            return new Shipping(country, city);
        }
        return new Shipping("", "");
    }

    private Payment extractPayment(String body) {
        Pattern pattern = Pattern.compile("Payment:\\s*(.+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(body);

        if (matcher.find()) {
            String value = matcher.group(1).trim();
            String type = value.toLowerCase().contains("invoice") ? "INVOICE" : "OTHER";

            Pattern termsPattern = Pattern.compile("(NET\\s*\\d+)", Pattern.CASE_INSENSITIVE);
            Matcher termsMatcher = termsPattern.matcher(value);
            String terms = termsMatcher.find() ? termsMatcher.group(1).trim().replaceAll("\\s+", "_") : "UNKNOWN";

            return new Payment(type, terms);
        }
        return new Payment("UNKNOWN", "UNKNOWN");
    }

    private String extractCurrency(String body) {
        Pattern pattern = Pattern.compile("Currency:\\s*(\\w+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(body);
        return matcher.find() ? matcher.group(1).trim() : "USD";
    }
}

