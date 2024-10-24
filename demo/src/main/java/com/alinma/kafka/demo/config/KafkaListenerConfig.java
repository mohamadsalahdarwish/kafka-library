package com.alinma.kafka.demo.config;

import com.alinma.kafka.demo.model.Product;
import com.alinma.rib.kafka.consumer.config.KafkaConsumerConfig;
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

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> msdKafkaListenerContainerFactory() {
        return kafkaConsumerConfig.kafkaListenerContainerFactory("msd", String.class, String.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> xyzKafkaListenerContainerFactory() {
        return kafkaConsumerConfig.kafkaListenerContainerFactory("xyz", String.class, String.class);
    }
}
