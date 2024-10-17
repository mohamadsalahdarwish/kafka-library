package com.alinma.rib.kafka.producer;

import com.alinma.rib.kafka.config.data.KafkaConfigData;
import com.alinma.rib.kafka.config.data.KafkaProducerConfigData;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig<K extends Serializable, V> {

    private final KafkaConfigData kafkaConfigData;
    private final KafkaProducerConfigData kafkaProducerConfigData;

    public KafkaProducerConfig(KafkaConfigData kafkaConfigData,
                               KafkaProducerConfigData kafkaProducerConfigData) {
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaProducerConfigData = kafkaProducerConfigData;
    }

    @Bean
    public Map<String, Object> commonProducerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigData.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProducerConfigData.getCommon().getKeySerializerClass());
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, kafkaProducerConfigData.getCommon().getCompressionType());
        props.put(ProducerConfig.ACKS_CONFIG, kafkaProducerConfigData.getCommon().getAcks());
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaProducerConfigData.getCommon().getBatchSize());
        props.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProducerConfigData.getCommon().getLingerMs());
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, kafkaProducerConfigData.getCommon().getRequestTimeoutMs());
        props.put(ProducerConfig.RETRIES_CONFIG, kafkaProducerConfigData.getCommon().getRetryCount());
        return props;
    }

    @Bean
    public Map<String, Object> avroProducerConfigs() {
        Map<String, Object> props = commonProducerConfigs();
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProducerConfigData.getAvro().getValueSerializerClass());
        props.put(kafkaConfigData.getSchemaRegistryUrlKey(), kafkaConfigData.getSchemaRegistryUrl());
        return props;
    }

    @Bean
    public Map<String, Object> jsonProducerConfigs() {
        Map<String, Object> props = commonProducerConfigs();
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
        return props;
    }

    @Bean
    public ProducerFactory<K, V> avroProducerFactory() {
        return new DefaultKafkaProducerFactory<>(avroProducerConfigs());
    }

    @Bean
    public ProducerFactory<K, V> jsonProducerFactory() {
        return new DefaultKafkaProducerFactory<>(jsonProducerConfigs());
    }

    @Bean
    public KafkaTemplate<K, V> avroKafkaTemplate() {
        return new KafkaTemplate<>(avroProducerFactory());
    }

    @Bean
    public KafkaTemplate<K, V> jsonKafkaTemplate() {
        return new KafkaTemplate<>(jsonProducerFactory());
    }
}