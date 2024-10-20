package com.alinma.kafka.demo.producer;

import com.alinma.rib.kafka.producer.service.KafkaProducer;
import com.alinma.rib.kafka.producer.service.KafkaSendCallback;
import com.alinma.rib.kafka.producer.service.impl.AvrokafkaProducerImpl;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

@Component
public class OrderProducerExample {

    private final KafkaProducer<String, SpecificRecordBase> kafkaProducer;

    @Autowired
    public OrderProducerExample(AvrokafkaProducerImpl<String, SpecificRecordBase> kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    public void sendOrderMessage(String key, SpecificRecordBase message) {
        kafkaProducer.send("order-topic", key, message, new KafkaSendCallback<String, SpecificRecordBase>() {
            @Override
            public void onSuccess(SendResult<String, SpecificRecordBase> result) {
                // Handle success
                System.out.println("Order message sent successfully to 'order-topic' with key: " + key);
            }

            @Override
            public void onFailure(Throwable ex) {
                // Handle failure
                System.err.println("Failed to send order message to 'order-topic' with key: " + key + ", exception: " + ex.getMessage());
            }
        });
    }
}
