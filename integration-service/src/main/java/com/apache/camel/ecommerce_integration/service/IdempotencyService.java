package com.apache.camel.ecommerce_integration.service;


//import org.springframework.stereotype.Service;
//
//import java.util.HashSet;
//import java.util.Set;
//
//@Service("idempotencyService")
//public class IdempotencyService {
//
//    private final Set<String> sentOrders = new HashSet<>();
//
//    public boolean isSent(String orderNumber) {
//        return sentOrders.contains(orderNumber);
//    }
//
//    public void markSent(String orderNumber) {
//        sentOrders.add(orderNumber);
//    }
//}


import com.apache.camel.ecommerce_integration.model.SentNotification;
import com.apache.camel.ecommerce_integration.repository.SentNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service("idempotencyService")
@RequiredArgsConstructor
public class IdempotencyService {

    private final SentNotificationRepository repository;

    public boolean isSent(String orderNumber) {
        return repository.existsByOrderNumber(orderNumber);
    }

    @Transactional
    public void markSent(String orderNumber) {
        if (!isSent(orderNumber)) {
            repository.save(new SentNotification(orderNumber, LocalDateTime.now()));
        }
    }
}
