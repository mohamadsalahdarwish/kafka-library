package com.alinma.rib.kafka.producer.service;

import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

public interface KafkaProducer<K, V> {
    CompletableFuture<SendResult<K, V>> send(String topicName, K key, V message, KafkaSendCallback<K, V> callback);
}
