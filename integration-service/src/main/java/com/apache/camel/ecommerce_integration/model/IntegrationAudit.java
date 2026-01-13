package com.apache.camel.ecommerce_integration.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class IntegrationAudit {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderSource source;

    @Column(name = "original_payload")
    private String originalPayload;

    @Column(name = "routed_payload")
    private String routedPayload;

    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    @Column(name = "target_queue")
    private String targetQueue;   // e.g., order.command

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public enum OrderSource {
        REST, FILE, EMAIL
    }

    public enum MessageStatus {
        RECEIVED, ROUTED, FAILED
    }
}
