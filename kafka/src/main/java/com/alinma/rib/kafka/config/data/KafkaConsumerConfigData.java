package com.alinma.rib.kafka.config.data;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;

/**
 * Configuration properties for Kafka consumer.
 */
@Data
@Configuration
@PropertySource("classpath:application-kafka.properties")
@ConfigurationProperties(prefix = "kafka-consumer-config")
public class KafkaConsumerConfigData {

    /**
     * Common configuration properties for Kafka consumers.
     */
    private CommonConfig common;

    /**
     * Configuration properties for specific consumer groups.
     * The key is the consumer group ID.
     */
    private Map<String, ConsumerGroupConfig> consumerGroups;

    /**
     * Common configuration properties for Kafka consumers.
     */
    @Data
    public static class CommonConfig {
        /**
         * Deserializer class for keys.
         */
        private String keyDeserializer;

        /**
         * Deserializer class for values.
         */
        private String valueDeserializer;

        /**
         * Offset reset policy.
         */
        private String autoOffsetReset;

        /**
         * Whether to use batch listener.
         */
        private boolean batchListener;

        /**
         * Whether to auto-start the consumer.
         */
        private boolean autoStartup;

        /**
         * Concurrency level for the consumer.
         */
        private int concurrencyLevel;

        /**
         * Session timeout in milliseconds.
         */
        private int sessionTimeoutMs;

        /**
         * Heartbeat interval in milliseconds.
         */
        private int heartbeatIntervalMs;

        /**
         * Maximum poll interval in milliseconds.
         */
        private int maxPollIntervalMs;

        /**
         * Poll timeout in milliseconds.
         */
        private long pollTimeoutMs;

        /**
         * Maximum number of records to poll.
         */
        private int maxPollRecords;

        /**
         * Default maximum partition fetch bytes.
         */
        private int maxPartitionFetchBytesDefault;

        /**
         * Boost factor for maximum partition fetch bytes.
         */
        private int maxPartitionFetchBytesBoostFactor;
    }

    /**
     * Configuration properties for a specific consumer group.
     */
    @Data
    public static class ConsumerGroupConfig {
        /**
         * Consumer group ID.
         */
        private String consumerGroupId;

        /**
         * Deserializer class for keys.
         */
        private String keyDeserializer;

        /**
         * Deserializer class for values.
         */
        private String valueDeserializer;

        /**
         * Concurrency level for the consumer group.
         */
        private Integer concurrencyLevel;

        /**
         * Topic ID for the consumer group.
         */
        private String topicId;

        /**
         * Additional properties for the consumer group.
         */
        private Map<String, Object> properties;
    }
}