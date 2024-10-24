package com.alinma.kafka.demo.producer;

import com.alinma.rib.kafka.producer.service.KafkaProducer;
import com.alinma.rib.kafka.producer.service.KafkaSendCallback;
import com.alinma.rib.kafka.producer.service.impl.JsonKafkaProducerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Component
public class XyzKafkaProducer {

    private final KafkaProducer<String, String> kafkaProducer;

    @Autowired
    public XyzKafkaProducer(JsonKafkaProducerImpl<String, String> kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    public void send(String key, String message) {
        kafkaProducer.send("xyz-topic", key, message, new KafkaSendCallback<String, String>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                // Handle success
                System.out.println("Message sent successfully to 'xyz-topic' with key: " + key);
            }

            @Override
            public void onFailure(Throwable ex) {
                // Handle failure
                System.err.println("Failed to send message to 'xyz-topic' with key: " + key + ", exception: " + ex.getMessage());
            }
        });
    }
}
