package com.apache.camel.ecommerce_integration.service;

import com.apache.camel.ecommerce_integration.model.OutboxEvent;
import com.apache.camel.ecommerce_integration.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.apache.camel.ProducerTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboxPublisher {

    private final OutboxEventRepository outboxEventRepository;
    private final ProducerTemplate producerTemplate;

    @Scheduled(fixedDelay = 5000) // every 5 seconds
    public void publishPendingEvents() {
        List<OutboxEvent> pending = outboxEventRepository.findByPublishedFalse();
        for (OutboxEvent e : pending) {
            // Send to Kafka topic based on eventType
            producerTemplate.sendBody("kafka:" + e.getEventType(), e.getPayload());
            e.setPublished(true);
            outboxEventRepository.save(e);
        }
    }
}
