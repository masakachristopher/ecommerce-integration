package com.ecommerce.order.service.impl;

import com.ecommerce.order.constant.KafkaConstants;
import com.ecommerce.order.dto.OrderCreatedEvent;
import com.ecommerce.order.dto.OrderData;
import com.ecommerce.order.dto.PayloadDto;
import com.ecommerce.order.dto.PriceQuote;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.model.OutboxEvent;
import com.ecommerce.order.model.Product;
import com.ecommerce.order.repository.OrderRepository;
import com.ecommerce.order.repository.OutboxEventRepository;
import com.ecommerce.order.service.OrderService;
import com.ecommerce.order.service.PricingService;
import com.ecommerce.order.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final PricingService pricingService;
    private final ProductService productService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;
//    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public Order create(com.ecommerce.order.dto.Order request) {
        try {
            List<Order.OrderItem> items = request.getItems().stream()
                    .map(i -> {
                        Product product = productService.getBySku(i.getSku());
                        PriceQuote quote = pricingService.quote(product, request.getCustomerRef(), request.getCurrency());
                        return new Order.OrderItem(product.getSku(), i.getQuantity(), quote.unitPrice());
                    }).toList();

            BigDecimal total = items.stream()
                    .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Order order = new Order();
            order.setOrderNumber("#" + System.currentTimeMillis());
            order.setCustomerId(request.getCustomerRef());
            order.setItems(items);
            order.setTotal(total);
            order.setStatus(Order.OrderStatus.CREATED);
            order.setCurrency(request.getCurrency());
            order.setExternalOrderNumber(request.getExternalOrderRef());
            return processOrderCreation(order);
        } catch (Exception e) {
            log.error("Failed to process order", e);
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void markInventoryReserved(Long orderId) {
        updateStatus(orderId, Order.OrderStatus.INVENTORY_RESERVED, KafkaConstants.ORDER_RESERVED_EVENT);
    }

    private Order processOrderCreation(Order order) throws JsonProcessingException {
        order.setStatus(Order.OrderStatus.CREATED);
        Order saved = orderRepository.save(order);

        // TODO: add email, phoneNumber
        OrderCreatedEvent orderCreatedEvent = OrderCreatedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(KafkaConstants.ORDER_CREATED_EVENT)
                .eventVersion(1)
                .eventTime(Instant.now())
                .producer(KafkaConstants.PRODUCER)
                .traceId(MDC.get("traceId"))
                .correlationId(saved.getOrderNumber())
                .data(OrderData.builder().order(order).build())
                .build();

        OutboxEvent evt = OutboxEvent.builder()
                .eventId(orderCreatedEvent.getEventId())                // same eventId
                .aggregateType(KafkaConstants.DOMAIN)
                .aggregateId(saved.getOrderNumber())
                .eventType(KafkaConstants.ORDER_CREATED_EVENT)
                .payload(objectMapper.writeValueAsString(orderCreatedEvent))
                .published(false)
                .createdAt(LocalDateTime.now())
                .build();

        outboxEventRepository.save(evt);

        log.info("Order {} created + OutboxEvent stored", saved.getOrderNumber());
        return saved;
    }

    private void updateStatus(Long orderId, Order.OrderStatus status, String eventType) {
        orderRepository.findById(orderId)
                .ifPresent(o -> {
                    o.setStatus(status);
                    orderRepository.save(o);
//                    kafkaTemplate.send("order-events", eventType, o);
                    kafkaTemplate.send(KafkaConstants.ORDER_RESERVED_TOPIC, eventType, o);
                    log.info("Order {} updated to {}", orderId, status);
                });
    }
}