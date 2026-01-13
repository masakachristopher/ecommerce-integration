package com.ecommerce.order.constant;

import org.springframework.beans.factory.annotation.Value;

public class KafkaConstants {

    @Value("${spring.application.name}")
    public static final String PRODUCER = "";

    public static final String DOMAIN = "order";

    public static final String ORDER_CREATED_EVENT = "order-created";
    public static final String ORDER_CREATED_TOPIC = getTopicName(ORDER_CREATED_EVENT);

    public static final String ORDER_RESERVED_EVENT = "inventory-reserved";
    public static final String ORDER_RESERVED_TOPIC = getTopicName(ORDER_RESERVED_EVENT);


    public static String getTopicName(String eventName) {
        return DOMAIN + "." + eventName;
    }
}
