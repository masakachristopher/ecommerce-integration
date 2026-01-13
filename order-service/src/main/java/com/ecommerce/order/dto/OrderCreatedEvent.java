package com.ecommerce.order.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class OrderCreatedEvent {

    // Event metadata
    private String eventId;         // Unique per event
    private String eventType;       // e.g., "order.created"
    private int eventVersion;       // Versioning for schema evolution
    private Instant eventTime;      // ISO timestamp of event creation
    private String producer;        // Who emitted the event
    private String traceId;         // Distributed tracing ID
    private String correlationId;   // Business workflow correlation

    // Event payload
    private OrderData data;
//    private PayloadDto data;

}

