package com.apache.camel.ecommerce_integration.route;

import com.apache.camel.ecommerce_integration.model.IntegrationAudit;
import com.apache.camel.ecommerce_integration.processor.*;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

@Component
@RequiredArgsConstructor
public class OrderEmailRoute extends RouteBuilder {

    private final EmailClassificationProcessor emailClassificationProcessor;
    private final EmailToOrderPayloadProcessor emailToOrderPayloadProcessor;
    private final OrderAuditProcessor orderAuditProcessor;

    @Value("${imap.username}")
    private String imapUsername;

    @Value("${imap.password}")
    private String imapPassword;

    @Value("${imap.folder-name}")
    private String imapFolderName;

    @Value("${imap.host}")
    private String imapHost;

    @Value("${imap.port}")
    private String imapPort;

    @Override
    public void configure() throws Exception {
        // Route to process order emails
        String sourceUri = "imaps://"+imapHost+"?" +
                "username="+imapUsername+"&" +
                "password="+imapPassword+"&" +
                "folderName="+imapFolderName+"&" +
                "unseen=true&" +
                "delete=false&" +
                "delay=60000";

        from(sourceUri)
                .routeId("order-email-route")
                .process(exchange -> {
                    String originalPayload = exchange.getIn().getBody(String.class);
                    exchange.setProperty("originalPayload", originalPayload);
                    exchange.setProperty("source", IntegrationAudit.OrderSource.EMAIL);
                })
                .process(emailClassificationProcessor)
                .choice()
                    .when(header("emailType").isEqualTo("ATTACHMENT_REVIEW"))
                        .to("direct:attachmentReview")
                    .when(header("emailType").isEqualTo("STRUCTURED"))
                        .process(emailToOrderPayloadProcessor)
                        .marshal().json(JsonLibrary.Jackson)
                        .convertBodyTo(String.class)
//                        .process(orderAuditProcessor)
                        .to("direct:autoOrder")
                    .when(header("emailType").isEqualTo("SEMI_STRUCTURED"))
                        .to("direct:draftOrder")
                    .otherwise()
                        .to("direct:manualReview");
    }
}

