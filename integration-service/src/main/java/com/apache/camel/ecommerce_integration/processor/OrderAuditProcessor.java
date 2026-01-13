package com.apache.camel.ecommerce_integration.processor;

import com.apache.camel.ecommerce_integration.constant.QueueConstants;
import com.apache.camel.ecommerce_integration.model.IntegrationAudit;
import com.apache.camel.ecommerce_integration.repository.IntegrationAuditRepository;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderAuditProcessor implements Processor {

    private final IntegrationAuditRepository auditRepository;

    @Override
    public void process(Exchange exchange) throws Exception {
        String routed = exchange.getIn().getBody(String.class);

        String original = exchange.getProperty("originalPayload", String.class);
        IntegrationAudit.OrderSource source = exchange.getProperty("source", IntegrationAudit.OrderSource.class);

        IntegrationAudit audit = new IntegrationAudit();
        audit.setSource(source);
        audit.setOriginalPayload(original);
        audit.setRoutedPayload(routed);
        audit.setTargetQueue(QueueConstants.ORDER_CREATE);
        auditRepository.save(audit);

        exchange.getIn().setBody(routed);
    }

}
