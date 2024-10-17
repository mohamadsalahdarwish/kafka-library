package com.alinma.rib.kafka.producer;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.io.Serializable;

@Component
public class KafkaMessageHelper<K extends Serializable, V> {

    public void handleSuccess(String topicName, K key, V value, SendResult<K, V> result) {
        System.out.println("Message sent successfully to topic: " + topicName + " with key: " + key + " and value: " + value);
    }

    public void handleFailure(String topicName, K key, V value, Throwable ex) {
        System.err.println("Error sending message to topic: " + topicName + " with key: " + key + " and value: " + value + ". Exception: " + ex.getMessage());
    }

    public ListenableFutureCallback<SendResult<K, V>> getCallback(String topicName, K key, V value) {
        return new ListenableFutureCallback<SendResult<K, V>>() {
            @Override
            public void onSuccess(SendResult<K, V> result) {
                handleSuccess(topicName, key, value, result);
            }

            @Override
            public void onFailure(Throwable ex) {
                handleFailure(topicName, key, value, ex);
            }
        };
    }
}