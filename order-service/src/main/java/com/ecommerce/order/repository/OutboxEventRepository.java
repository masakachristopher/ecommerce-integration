package com.ecommerce.order.repository;

import com.ecommerce.order.model.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {
    List<OutboxEvent> findTop50ByPublishedFalseOrderByCreatedAt();

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        UPDATE OutboxEvent e
        SET e.published = true
        WHERE e.id = :id AND e.published = false
    """)
    int markPublished(@Param("id") Long id);
}
