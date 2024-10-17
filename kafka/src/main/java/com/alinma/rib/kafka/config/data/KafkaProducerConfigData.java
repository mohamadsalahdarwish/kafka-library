package com.alinma.rib.kafka.config.data;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "kafka-producer-config")
public class KafkaProducerConfigData {
    // Common configuration settings for all Kafka producers
    private CommonConfig common;

    // Configuration settings for Avro serializer
    private SerializerConfig avro;

    // Configuration settings for JSON serializer
    private SerializerConfig json;

    @Data
    public static class CommonConfig {
        // Serializer class for keys
        private String keySerializerClass;

        // Compression type for the producer
        private String compressionType;

        // Acknowledgment setting for the producer
        private String acks;

        // Batch size for the producer
        private Integer batchSize;

        // Boost factor for the batch size
        private Integer batchSizeBoostFactor;

        // Linger time in milliseconds before sending a batch
        private Integer lingerMs;

        // Request timeout in milliseconds
        private Integer requestTimeoutMs;

        // Number of retries for the producer
        private Integer retryCount;
    }

    @Data
    public static class SerializerConfig {
        // Serializer class for values
        private String valueSerializerClass;
    }
}