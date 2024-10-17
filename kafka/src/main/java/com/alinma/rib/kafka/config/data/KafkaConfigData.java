package com.alinma.rib.kafka.config.data;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "kafka-config")
public class KafkaConfigData {
    // Kafka bootstrap servers
    private String bootstrapServers;

    // Key for the schema registry URL
    private String schemaRegistryUrlKey;

    // URL for the schema registry
    private String schemaRegistryUrl;

    // Number of partitions for Kafka topics
    private Integer numOfPartitions;

    // Replication factor for Kafka topics
    private Short replicationFactor;
}