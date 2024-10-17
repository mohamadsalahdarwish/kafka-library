package com.alinma.rib.kafka.config.data;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "kafka-consumer-config")
public class KafkaConsumerConfigData {
    // Common configuration settings for all Kafka consumers
    private CommonConfig common;

    // Configuration settings for consumer group
    private Map<String, ConsumerGroupConfig> consumerGroups;


    @Data
    public static class CommonConfig {
        // Deserializer class for keys
        private String keyDeserializer;

        // Deserializer class for values
        private String valueDeserializer;

        // Behavior when there is no initial offset or if the current offset does not exist
        private String autoOffsetReset;

        // Indicates whether the consumer should use batch listeners
        private Boolean batchListener;

        // Indicates whether the consumer should start automatically
        private Boolean autoStartup;

        // Number of concurrent threads for the consumer
        private Integer concurrencyLevel;

        // Timeout used to detect consumer failures
        private Integer sessionTimeoutMs;

        // Interval at which the consumer sends heartbeats to the Kafka broker
        private Integer heartbeatIntervalMs;

        // Maximum delay between invocations of poll() when using consumer group management
        private Integer maxPollIntervalMs;

        // Timeout for the poll() method
        private Long pollTimeoutMs;

        // Maximum number of records returned in a single call to poll()
        private Integer maxPollRecords;

        // Default maximum amount of data per partition the server will return
        private Integer maxPartitionFetchBytesDefault;

        // Boost factor for the maximum amount of data per partition
        private Integer maxPartitionFetchBytesBoostFactor;


    }

    @Data
    public static class ConsumerGroupConfig {
        // ID of the consumer group
        private String consumerGroupId;

        // Deserializer class for values
        private String valueDeserializer;

        private Integer concurrencyLevel;

        // Key for the specific Avro reader
        private String specificAvroReaderKey;

        // Specific Avro reader
        private String specificAvroReader;

        private String topicId;
    }
}