package com.alinma.rib.kafka.producer.service;

import org.springframework.kafka.support.SendResult;

public interface KafkaSendCallback<K, V> {
    void onSuccess(SendResult<K, V> result);
    void onFailure(Throwable ex);
}
