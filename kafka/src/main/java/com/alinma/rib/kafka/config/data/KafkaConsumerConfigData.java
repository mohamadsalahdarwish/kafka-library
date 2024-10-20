package com.alinma.rib.kafka.config.data;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "kafka-consumer-config")
public class KafkaConsumerConfigData {

    private CommonConfig common;
    private Map<String, ConsumerGroupConfig> consumerGroups;

    @Data
    public static class CommonConfig {
        private String keyDeserializer;
        private String valueDeserializer;
        private String autoOffsetReset;
        private boolean batchListener;
        private boolean autoStartup;
        private int concurrencyLevel;
        private int sessionTimeoutMs;
        private int heartbeatIntervalMs;
        private int maxPollIntervalMs;
        private long pollTimeoutMs;
        private int maxPollRecords;
        private int maxPartitionFetchBytesDefault;
        private int maxPartitionFetchBytesBoostFactor;
    }

    @Data
    public static class ConsumerGroupConfig {
        private String consumerGroupId;
        private String keyDeserializer;
        private String valueDeserializer;
        private Integer concurrencyLevel;
        private String topicId;
        private Map<String, Object> properties;
    }
}
