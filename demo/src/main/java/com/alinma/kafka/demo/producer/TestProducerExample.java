package com.alinma.kafka.demo.producer;

import com.alinma.rib.kafka.producer.service.KafkaProducer;
import com.alinma.rib.kafka.producer.service.KafkaSendCallback;
import com.alinma.rib.kafka.producer.service.impl.JsonKafkaProducerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.kafka.support.SendResult;

@Component
public class TestProducerExample {

    private final KafkaProducer<String, String> kafkaProducer;

    @Autowired
    public TestProducerExample(JsonKafkaProducerImpl<String, String> kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    public void send(String key, String message) {
        kafkaProducer.send("test-topic", key, message, new KafkaSendCallback<String, String>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                // Handle success
                System.out.println("Message sent successfully to 'test-topic' with key: " + key);
            }

            @Override
            public void onFailure(Throwable ex) {
                // Handle failure
                System.err.println("Failed to send message to 'test-topic' with key: " + key + ", exception: " + ex.getMessage());
            }
        });
    }
}
