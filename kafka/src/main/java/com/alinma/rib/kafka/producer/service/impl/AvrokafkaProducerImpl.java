package com.alinma.rib.kafka.producer.service.impl;

import com.alinma.rib.kafka.producer.exception.KafkaProducerException;
import com.alinma.rib.kafka.producer.service.KafkaProducer;
import com.alinma.rib.kafka.producer.service.KafkaSendCallback;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class AvrokafkaProducerImpl<K extends Serializable, V> implements KafkaProducer<K, V> {

    private final KafkaTemplate<K, V> kafkaTemplate;

    public AvrokafkaProducerImpl(@Qualifier("avroKafkaTemplate") KafkaTemplate<K, V> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public CompletableFuture<SendResult<K, V>> send(String topicName, K key, V message, KafkaSendCallback<K, V> callback) {
        log.info("Sending message={} to topic={}", message, topicName);
        try {
            // Send the message using KafkaTemplate
            CompletableFuture<SendResult<K, V>> future = kafkaTemplate.send(topicName, key, message);

            // Attach the callback
            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    // Handle failure
                    log.error("Failed to send message with key: {}, exception: {}", key, ex.getMessage());
                    callback.onFailure(ex);
                } else {
                    // Handle success
                    log.info("Successfully sent message with key: {}, result: {}", key, result);
                    callback.onSuccess(result);
                }
            });

            return future;
        } catch (KafkaException e) {
            log.error("Error on Kafka producer with key: {}, message: {} and exception: {}", key, message, e.getMessage());
            // Invoke the failure callback
            callback.onFailure(e);
            throw new KafkaProducerException("Error on Kafka producer with key: " + key + " and message: " + message);
        }
    }

    @PreDestroy
    public void close() {
        log.info("Closing Avro Kafka producer!");
        kafkaTemplate.destroy();
    }
}
