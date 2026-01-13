package com.apache.camel.ecommerce_integration.repository;

import com.apache.camel.ecommerce_integration.model.IntegrationAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntegrationAuditRepository extends JpaRepository<IntegrationAudit, Long> {
}
