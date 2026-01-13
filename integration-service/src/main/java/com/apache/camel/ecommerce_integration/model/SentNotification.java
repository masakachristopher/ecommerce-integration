package com.apache.camel.ecommerce_integration.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_sent")
@NoArgsConstructor
@AllArgsConstructor
@ToString @Getter @Setter
public class SentNotification {

    @Id
    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

}

