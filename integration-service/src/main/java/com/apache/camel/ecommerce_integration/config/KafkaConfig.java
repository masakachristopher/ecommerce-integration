package com.apache.camel.ecommerce_integration.config;

import org.apache.camel.component.kafka.KafkaComponent;
import org.apache.camel.component.kafka.KafkaConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

//    @Value("${camel.component.kafka.brokers}")
//    private String brokers;
    @Value("${spring.kafka.brokers}")
    private String brokers;

    @Bean
    public KafkaComponent kafka() {
        KafkaConfiguration configuration = new KafkaConfiguration();
        configuration.setBrokers(brokers);
        KafkaComponent kafka = new KafkaComponent();
        kafka.setConfiguration(configuration);
        return kafka;
    }
}
