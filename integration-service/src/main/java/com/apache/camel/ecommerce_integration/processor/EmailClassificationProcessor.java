package com.apache.camel.ecommerce_integration.processor;

import com.apache.camel.ecommerce_integration.constant.OrderEmailSignals;
import com.apache.camel.ecommerce_integration.helper.OrderEmailScore;
import com.apache.camel.ecommerce_integration.model.OrderEmailType;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.attachment.Attachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Matcher;

import static com.apache.camel.ecommerce_integration.constant.RegexPattern.ORDER_NUMBER_PATTERN;


@Component
@RequiredArgsConstructor
public class EmailClassificationProcessor implements Processor {

    private static final Logger log = LoggerFactory.getLogger(EmailClassificationProcessor.class);

    @Override
    public void process(Exchange exchange) {
        OrderEmailScore score = new OrderEmailScore();

        if (hasReference(exchange)) {
            log.info("Detected ORDER_REFERENCE");
            score.add(OrderEmailSignals.ORDER_REFERENCE);
            exchange.getIn().setHeader("hasReference", true);
        }

        if (hasSkuAndQty(exchange)) {
            log.info("Detected SKU_AND_QUANTITY");
            score.add(OrderEmailSignals.SKU_AND_QUANTITY);
        }

        if (hasDeliveryAddress(exchange)) {
            log.info("Detected DELIVERY_ADDRESS");
            score.add(OrderEmailSignals.DELIVERY_ADDRESS);
        }

        if (hasPaymentTerms(exchange)) {
            log.info("Detected PAYMENT_TERMS");
            score.add(OrderEmailSignals.PAYMENT_TERMS);
        }

        if (hasAttachment(exchange)) {
            log.info("Detected ATTACHMENT_PRESENT");
            score.add(OrderEmailSignals.ATTACHMENT_PRESENT);
        }

        log.info("classification score: {}", score.value());

        exchange.getIn().setHeader("totalscore", score.value());

        OrderEmailType type = score.classify();

        exchange.getIn().setHeader("emailScore", score.value());
        exchange.getIn().setHeader("emailType", type);

    }

    public static Optional<String> extractReference(String subject) {
        if (subject == null) return Optional.empty();
        Matcher matcher = ORDER_NUMBER_PATTERN.matcher(subject);
        if (matcher.find()) {
            return Optional.of(matcher.group(1));
        }
        return Optional.empty();
    }

    private boolean hasReference(Exchange exchange) {
        String subject = exchange.getIn().getHeader("subject", String.class);
        String body = exchange.getIn().getBody(String.class);
        log.info("concat: {}{}", subject, body);
        Optional<String> reference = extractReference(subject);
        exchange.getIn().setHeader("orderReference", reference.orElse(null));
        return subject.toLowerCase().matches(".*(ref|reference|po|order)[^\\d]*\\d+.*");
    }

    private boolean hasSkuAndQty(Exchange exchange) {
        String body = exchange.getIn().getBody(String.class);
        return body != null && body.toLowerCase().matches("(?s).*sku[- ]?\\d+.*(x|qty|quantity)\\s*\\d+.*");
    }

    private boolean hasDeliveryAddress(Exchange exchange) {
        String body = exchange.getIn().getBody(String.class);
        return body != null && (body.toLowerCase().contains("deliver") || body.toLowerCase().contains("address") || body.toLowerCase().contains("ship"));
    }

    private boolean hasPaymentTerms(Exchange exchange) {
        String body = exchange.getIn().getBody(String.class);
        return body != null && (body.toLowerCase().contains("invoice") || body.toLowerCase().contains("net") || body.toLowerCase().contains("bank transfer"));
    }

    private boolean hasAttachment(Exchange exchange) {
        return exchange.getIn(Attachment.class) != null;
    }

}

