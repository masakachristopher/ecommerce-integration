// package com.apache.camel.ecommerce_integration.route;

// import com.apache.camel.ecommerce_integration.dto.OrderCreatedPayload;
// import com.apache.camel.ecommerce_integration.service.IdempotencyService;
// import com.apache.camel.ecommerce_integration.util.NumberFormatter;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.apache.camel.Exchange;
// import org.apache.camel.builder.RouteBuilder;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Component;

// import java.math.BigDecimal;

// @Slf4j
// @Component
// @RequiredArgsConstructor
// public class OrderNotificationRoute extends RouteBuilder {

//     private final ObjectMapper objectMapper;
//     private final IdempotencyService idempotencyService;
//     private final NumberFormatter numberFormatter;

//     @Value("${smtp.username}")
//     private String smtpUsername;

//     @Value("${smtp.password}")
//     private String smtpPassword;

//     @Override
//     public void configure() throws Exception {

//         // Handle deserialization errors
//         onException(Exception.class)
//                 .log("Failed to send notification: ${exception.message}")
//                 .handled(true);

//         from("kafka:order.order-created?groupId=integration-service")
//                 .routeId("notification-kafka-consumer")
//                 .log("Received Kafka message: ${body}")
//                 // Deserialize JSON payload
//                 .process(exchange -> {
//                     String json = exchange.getIn().getBody(String.class);
//                     OrderCreatedPayload payload = objectMapper.readValue(json, OrderCreatedPayload.class);
//                     exchange.setProperty("payload", payload);
//                     // Idempotency check
//                     if (idempotencyService.isSent(payload.orderNumber())) {
//                         log.info("Notification already sent for order {}", payload.orderNumber());
//                         exchange.setProperty(Exchange.ROUTE_STOP, true);
//                     }
//                 })
//                 .process(exchange -> {
//                     OrderCreatedPayload payload = exchange.getProperty("payload", OrderCreatedPayload.class);
//                     // Set headers and body
//                     exchange.getIn().setHeader("subject", "Your Order " + payload.orderNumber() + " is Confirmed!");
//                     exchange.getIn().setHeader("to", payload.customerEmail());
//                     exchange.getIn().setBody("Hello " + (payload.customerName() != null ? payload.customerName() : "Customer") + ",\n\n" +

//                             "Thank you for your order! We're excited to let you know it has been successfully received.\n\n" +

//                             "Order Details:\n" +
//                             "   Order Number: " + payload.orderNumber() + "\n" +
//                             "   Total Amount: " + numberFormatter.formatAmount(new BigDecimal(payload.totalAmount()), payload.currency()) + " " + payload.currency() + "\n\n" +

//                             "Thank you for shopping with us!\n" +
//                             "Warm regards,\n" +
//                             "2316 Inc.\n" +
//                             "support@2316inc.com | www.2316inc.com");
//                 })
//                 // Send email
//                 .to("smtp://smtp.gmail.com:587?username="+smtpUsername+"&password="+smtpPassword+"&to=${header.to}&from="+smtpUsername+"&mail.smtp.auth=true&mail.smtp.starttls.enable=true&debugMode=false")
//                 .process(exchange -> {
//                         OrderCreatedPayload payload = exchange.getProperty("payload", OrderCreatedPayload.class);
//                         log.info("Notification sent for order {}", payload.orderNumber());
//                         // mark as sent
//                         idempotencyService.markSent(payload.orderNumber());
//                     });
//     }
// }