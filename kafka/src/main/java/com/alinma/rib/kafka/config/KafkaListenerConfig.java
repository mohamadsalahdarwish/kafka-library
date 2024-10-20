package com.alinma.rib.kafka.config;

import com.alinma.rib.kafka.consumer.config.KafkaConsumerConfig;
import com.alinma.rib.kafka.order.avro.model.Product;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

@Configuration
public class KafkaListenerConfig {

    private final KafkaConsumerConfig kafkaConsumerConfig;

    public KafkaListenerConfig(KafkaConsumerConfig kafkaConsumerConfig) {
        this.kafkaConsumerConfig = kafkaConsumerConfig;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> paymentKafkaListenerContainerFactory() {
        return kafkaConsumerConfig.kafkaListenerContainerFactory("payment", String.class, String.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Product> orderKafkaListenerContainerFactory() {
        return kafkaConsumerConfig.kafkaListenerContainerFactory("order", String.class, Product.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> testKafkaListenerContainerFactory() {
        return kafkaConsumerConfig.kafkaListenerContainerFactory("test", String.class, String.class);
    }
}
