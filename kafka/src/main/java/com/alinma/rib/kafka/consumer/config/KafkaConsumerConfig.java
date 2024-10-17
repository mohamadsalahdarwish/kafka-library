package com.alinma.rib.kafka.consumer.config;

import com.alinma.rib.kafka.config.data.KafkaConfigData;
import com.alinma.rib.kafka.config.data.KafkaConsumerConfigData;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig<K extends Serializable, V> {

    private final KafkaConfigData kafkaConfigData;
    private final KafkaConsumerConfigData kafkaConsumerConfigData;

    public KafkaConsumerConfig(KafkaConfigData kafkaConfigData,
                               KafkaConsumerConfigData kafkaConsumerConfigData) {
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaConsumerConfigData = kafkaConsumerConfigData;
    }

    private Map<String, Object> commonConsumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        KafkaConsumerConfigData.CommonConfig common = kafkaConsumerConfigData.getCommon();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigData.getBootstrapServers());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, common.getKeyDeserializer());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, common.getAutoOffsetReset());
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, common.getSessionTimeoutMs());
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, common.getHeartbeatIntervalMs());
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, common.getMaxPollIntervalMs());
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG,
                common.getMaxPartitionFetchBytesDefault() * common.getMaxPartitionFetchBytesBoostFactor());
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, common.getMaxPollRecords());
        return props;
    }

    private Map<String, Object> consumerConfigs(String consumerGroup) {
        Map<String, Object> props = new HashMap<>(commonConsumerConfigs());
        KafkaConsumerConfigData.CommonConfig common = kafkaConsumerConfigData.getCommon();
        KafkaConsumerConfigData.ConsumerGroupConfig groupConfig = kafkaConsumerConfigData.getConsumerGroups().get(consumerGroup);

        // Apply common value deserializer
        String valueDeserializer = common.getValueDeserializer();

        // Override if specific value deserializer is provided for the consumer group
        if (groupConfig.getValueDeserializer() != null) {
            valueDeserializer = groupConfig.getValueDeserializer();
        }

        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);

        // Special handling for JSON deserializer
        if (valueDeserializer.equals(JsonDeserializer.class.getName())) {
            props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
            // Wrap with ErrorHandlingDeserializer
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
            props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        }

        // Add group-specific configurations
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupConfig.getConsumerGroupId());

        if (groupConfig.getSpecificAvroReaderKey() != null && groupConfig.getSpecificAvroReader() != null) {
            props.put(groupConfig.getSpecificAvroReaderKey(), groupConfig.getSpecificAvroReader());
        }

        if (kafkaConfigData.getSchemaRegistryUrlKey() != null && kafkaConfigData.getSchemaRegistryUrl() != null) {
            props.put(kafkaConfigData.getSchemaRegistryUrlKey(), kafkaConfigData.getSchemaRegistryUrl());
        }

        return props;
    }

    @Bean
    @Lazy
    public <T> ConcurrentKafkaListenerContainerFactory<K, T> kafkaListenerContainerFactory() {
        return new ConcurrentKafkaListenerContainerFactory<>();
    }

    @Lazy
    public <T> ConcurrentKafkaListenerContainerFactory<K, T> kafkaListenerContainerFactory(String consumerGroup) {
        KafkaConsumerConfigData.CommonConfig common = kafkaConsumerConfigData.getCommon();
        KafkaConsumerConfigData.ConsumerGroupConfig groupConfig = kafkaConsumerConfigData.getConsumerGroups().get(consumerGroup);

        ConcurrentKafkaListenerContainerFactory<K, T> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(consumerGroup));
        factory.setBatchListener(common.getBatchListener());
        factory.setAutoStartup(common.getAutoStartup());

        // Determine concurrency level (override if specified in group config)
        int concurrencyLevel = groupConfig.getConcurrencyLevel() != null
                ? groupConfig.getConcurrencyLevel()
                : common.getConcurrencyLevel();
        factory.setConcurrency(concurrencyLevel);

        factory.getContainerProperties().setPollTimeout(common.getPollTimeoutMs());
        return factory;
    }

    private <T> ConsumerFactory<K, T> consumerFactory(String consumerGroup) {
        Map<String, Object> configs = consumerConfigs(consumerGroup);
        return new DefaultKafkaConsumerFactory<>(configs);
    }
}
