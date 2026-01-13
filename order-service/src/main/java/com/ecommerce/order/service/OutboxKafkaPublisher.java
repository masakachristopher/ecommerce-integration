package com.ecommerce.order.service;

import com.ecommerce.order.model.OutboxEvent;
import com.ecommerce.order.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import com.ecommerce.order.constant.KafkaConstants;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxKafkaPublisher {

    private final OutboxEventRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private TransactionTemplate txTemplate;

    private static final String TOPIC = KafkaConstants.ORDER_CREATED_TOPIC;

    /**
     * Poll unpublished outbox events and publish to Kafka.
     * Not transactional by design.
     */
    @Transactional
    @Scheduled(fixedDelay = 5000)
    public void publish() {

        List<OutboxEvent> events = outboxRepository.findTop50ByPublishedFalseOrderByCreatedAt();

        if (events.isEmpty()) {
            return;
        }

        log.debug("Found {} unpublished outbox events", events.size());

//        SYNC/BLOCKING SOLUTION
        for (OutboxEvent evt : events) {
            try {
                // blocking send ensures we stay in Spring thread
                kafkaTemplate.send(TOPIC, evt.getAggregateId(), evt.getPayload()).get();

                // transactional update in same thread
                int updated = outboxRepository.markPublished(evt.getId());

                if (updated == 1) {
                    log.info("OutboxEvent {} published successfully", evt.getId());
                } else {
                    log.warn("OutboxEvent {} already marked as published", evt.getId());
                }

                log.info("OutboxEvent {} published and marked successfully", evt.getId());

            } catch (Exception e) {
                log.error("Failed to publish OutboxEvent {} (will retry)", evt.getId(), e);
            }
        }


//        ASYNC SOLUTION
//        for (OutboxEvent evt : events) {
//
//            kafkaTemplate
//                    .send(TOPIC, evt.getAggregateId(), evt.getPayload())
//                    .whenComplete((result, ex) -> {
//
//                        if (ex == null) {
//                            // ensure transaction exists in this thread
//                            txTemplate.execute(status -> {
////                                outboxRepository.markPublished(evt.getId());
//                                markAsPublished(evt, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
//                                return null;
//                            });
//                            log.info("OutboxEvent {} marked published [async]", evt.getId());
//                        } else {
//                            // need action like retry
//                            log.error("Kafka publish failed for {}", evt.getId(), ex);
//                        }
//            });
//
//        }
    }

    /**
     * Mark event as published only after Kafka ACK.
     */
    @Transactional
    private void markAsPublished(OutboxEvent evt, int partition, long offset
    ) {
        try {
            int updated = outboxRepository.markPublished(evt.getId());

            if (updated == 1) {
                log.info("OutboxEvent {} published successfully [partition={}, offset={}]", evt.getId(), partition, offset);
            } else {
                log.warn("OutboxEvent {} already marked as published (possible concurrent publisher)", evt.getId());
            }
        } catch (Exception e) {
            log.error("Failed to update published flag for OutboxEvent {}", evt.getId(), e);
        }
    }
}