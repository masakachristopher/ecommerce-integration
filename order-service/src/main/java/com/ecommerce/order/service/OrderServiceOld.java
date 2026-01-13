//package com.ecommerce.order.service;
//
//import com.ecommerce.order.dto.PayloadDto;
//import com.ecommerce.order.dto.PriceQuote;
//import com.ecommerce.order.model.Order;
//import com.ecommerce.order.model.OutboxEvent;
//import com.ecommerce.order.model.Product;
//import com.ecommerce.order.repository.OrderRepository;
//import com.ecommerce.order.repository.OutboxEventRepository;
//import com.ecommerce.order.repository.ProductRepository;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class OrderServiceOld {
//
//    private final OrderRepository orderRepository;
//    private final OutboxEventRepository outboxEventRepository;
//    private final ProductRepository productRepository;
//    private final PricingService pricingService;
//    private final ProductService productService;
//
//    private final KafkaTemplate<String, Object> kafkaTemplate;
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
////    @Transactional
////    public Order create1(CreateOrderRequest request) {
////        try {
////            Order order = Order.builder()
////                    .orderNumber("ORD-" + System.currentTimeMillis())
////                    .customerId(request.customerId())
////                    .total(request.totalAmount())
////                    .items(request.items().stream()
////                            .map(i -> new Order.OrderItem(i.productId(), i.quantity(), i.unitPrice()))
////                            .toList())
////                    .status(Order.OrderStatus.CREATED)
////                    .build();
////
////            return processOrderCreation(order);
////        } catch (Exception e) {
////            log.error("Failed to process order", e);
////            throw new RuntimeException(e);
////        }
////    }
//
//    @Transactional
//    public Order create(com.ecommerce.order.dto.Order request) {
//
//        try {
//            List<Order.OrderItem> items = request.getItems().stream()
//                    .map(i -> {
//                        Product product = productService.getBySku(i.getSku());
//                        PriceQuote quote = pricingService.quote(product, request.getCustomerRef(), request.getCurrency());
////                        BigDecimal price = pricingService.getUnitPrice(product, request.getCustomerRef(), request.getCurrency());
////                        return new Order.OrderItem(product.getSku(), i.getQuantity(), price);
//                        return new Order.OrderItem(product.getSku(), i.getQuantity(), quote.unitPrice());
//                    }).toList();
//
//
//            BigDecimal total = items.stream()
//                    .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
//                    .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//            Order order = new Order();
//            order.setOrderNumber("#" + System.currentTimeMillis());
//            order.setCustomerId(request.getCustomerRef());
//            order.setItems(items);
//            order.setTotal(total);
//            order.setStatus(Order.OrderStatus.CREATED);
//            order.setCurrency(request.getCurrency());
//            return processOrderCreation(order);
//        } catch (Exception e) {
//            log.error("Failed to process order", e);
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Transactional
//    public void markInventoryReserved(Long orderId) {
//        updateStatus(orderId, Order.OrderStatus.INVENTORY_RESERVED, "InventoryReserved");
//    }
//
//    private Order processOrderCreation(Order order) throws JsonProcessingException {
//        order.setStatus(Order.OrderStatus.CREATED);
//        Order saved = orderRepository.save(order);
//
//        String payload = objectMapper.writeValueAsString(new PayloadDto(
//                saved.getOrderNumber(),
//                saved.getCustomerId(),
//                saved.getTotal(),
//                saved.getStatus().toString(),
//                saved.getCurrency(),
//                "masakachristopher@yahoo.com",
//                saved.getExternalOrderNumber(),
//                saved.getItems()
//        ));
//
//        OutboxEvent evt = OutboxEvent.builder()
//                .aggregateType("Order")
//                .aggregateId("ORD-" + System.currentTimeMillis())
//                .eventType("OrderCreated")
//                .payload(payload)
//                .published(false)
//                .createdAt(LocalDateTime.now())
//                .build();
//
//        outboxEventRepository.save(evt);
//
//        log.info("Order {} created + OutboxEvent stored", saved.getOrderNumber());
//
//        return saved;
//    }
//
//    private void updateStatus(Long orderId, Order.OrderStatus status, String eventType) {
//        orderRepository.findById(orderId)
//                .ifPresent(o -> {
//                    o.setStatus(status);
//                    orderRepository.save(o);
//                    kafkaTemplate.send("order-events", eventType, o);
//                    log.info("Order {} updated to {}", orderId, status);
//                });
//    }
//
//}