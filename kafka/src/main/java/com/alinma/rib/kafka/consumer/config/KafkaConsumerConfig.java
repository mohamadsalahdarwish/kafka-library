package com.alinma.rib.kafka.consumer.config;

import com.alinma.rib.kafka.config.data.KafkaConfigData;
import com.alinma.rib.kafka.config.data.KafkaConsumerConfigData;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    private final KafkaConfigData kafkaConfigData;
    private final KafkaConsumerConfigData consumerConfigData;

    public KafkaConsumerConfig(KafkaConfigData kafkaConfigData,
                               KafkaConsumerConfigData consumerConfigData) {
        this.kafkaConfigData = kafkaConfigData;
        this.consumerConfigData = consumerConfigData;
    }

    private Map<String, Object> getCommonConfigs() {
        KafkaConsumerConfigData.CommonConfig common = consumerConfigData.getCommon();
        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigData.getBootstrapServers());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, common.getAutoOffsetReset());
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, common.getSessionTimeoutMs());
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, common.getHeartbeatIntervalMs());
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, common.getMaxPollIntervalMs());
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG,
                common.getMaxPartitionFetchBytesDefault() * common.getMaxPartitionFetchBytesBoostFactor());
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, common.getMaxPollRecords());

        String schemaRegistryUrlKey = kafkaConfigData.getSchemaRegistryUrlKey();
        String schemaRegistryUrl = kafkaConfigData.getSchemaRegistryUrl();
        if (schemaRegistryUrlKey != null && schemaRegistryUrl != null) {
            props.put(schemaRegistryUrlKey, schemaRegistryUrl);
        }

        return props;
    }

    private Map<String, Object> getConsumerConfigs(String groupId) {
        Map<String, Object> props = new HashMap<>(getCommonConfigs());
        KafkaConsumerConfigData.ConsumerGroupConfig groupConfig = consumerConfigData.getConsumerGroups().get(groupId);

        if (groupConfig != null) {
            String keyDeserializer = groupConfig.getKeyDeserializer() != null
                    ? groupConfig.getKeyDeserializer()
                    : consumerConfigData.getCommon().getKeyDeserializer();
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);

            String valueDeserializer = groupConfig.getValueDeserializer() != null
                    ? groupConfig.getValueDeserializer()
                    : consumerConfigData.getCommon().getValueDeserializer();
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);

            props.put(ConsumerConfig.GROUP_ID_CONFIG, groupConfig.getConsumerGroupId());

            if (groupConfig.getProperties() != null) {
                props.putAll(groupConfig.getProperties());
            }

            handleJsonAndAvroDeserializer(props, valueDeserializer);
        }

        return props;
    }

    private void handleJsonAndAvroDeserializer(Map<String, Object> props, String valueDeserializer) {
        if (JsonDeserializer.class.getName().equals(valueDeserializer)) {
            props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class.getName());
            props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
        } else if ("io.confluent.kafka.serializers.KafkaAvroDeserializer".equals(valueDeserializer)) {
            props.put("specific.avro.reader", true); // Ensure Avro specific reader is enabled
        }
    }

    public <K, V> ConcurrentKafkaListenerContainerFactory<K, V> kafkaListenerContainerFactory(
            String groupId, Class<K> keyClass, Class<V> valueClass) {

        Map<String, Object> configs = getConsumerConfigs(groupId);
        ConsumerFactory<K, V> consumerFactory = new DefaultKafkaConsumerFactory<>(configs);

        ConcurrentKafkaListenerContainerFactory<K, V> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);

        KafkaConsumerConfigData.CommonConfig common = consumerConfigData.getCommon();
        KafkaConsumerConfigData.ConsumerGroupConfig groupConfig = consumerConfigData.getConsumerGroups().get(groupId);

        factory.setAutoStartup(common.isAutoStartup());
        factory.setBatchListener(common.isBatchListener());
        factory.getContainerProperties().setPollTimeout(common.getPollTimeoutMs());

        int concurrencyLevel = (groupConfig != null && groupConfig.getConcurrencyLevel() != null)
                ? groupConfig.getConcurrencyLevel()
                : common.getConcurrencyLevel();
        factory.setConcurrency(concurrencyLevel);

        return factory;
    }
}
