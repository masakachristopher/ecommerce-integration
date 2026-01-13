package com.apache.camel.ecommerce_integration.repository;

import com.apache.camel.ecommerce_integration.model.SentNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SentNotificationRepository extends JpaRepository<SentNotification, String> {
    boolean existsByOrderNumber(String orderNumber);
}

