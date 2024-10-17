package com.alinma.rib.kafka.config;

import com.alinma.rib.kafka.consumer.config.KafkaConsumerConfig;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

@Configuration
public class KafkaListenerConfig {

    private final KafkaConsumerConfig<String, String> kafkaConsumerConfig;

    public KafkaListenerConfig(KafkaConsumerConfig<String, String> kafkaConsumerConfig) {
        this.kafkaConsumerConfig = kafkaConsumerConfig;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> paymentKafkaListenerContainerFactory() {
        return kafkaConsumerConfig.kafkaListenerContainerFactory("payment");
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SpecificRecordBase> orderKafkaListenerContainerFactory() {
        return kafkaConsumerConfig.kafkaListenerContainerFactory("order");
    }
}