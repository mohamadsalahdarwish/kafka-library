package com.alinma.kafka.demo.producer;

import com.alinma.rib.kafka.producer.KafkaMessageHelper;
import com.alinma.rib.kafka.producer.service.KafkaProducer;
import com.alinma.rib.kafka.producer.service.impl.JsonkafkaProducerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentProducerExample {

    private final KafkaProducer<String, String> kafkaProducer;
    private final KafkaMessageHelper<String, String> kafkaMessageHelper;

    @Autowired
    public PaymentProducerExample(JsonkafkaProducerImpl<String, String> kafkaProducer, KafkaMessageHelper<String, String> kafkaMessageHelper) {
        this.kafkaProducer = kafkaProducer;
        this.kafkaMessageHelper = kafkaMessageHelper;
    }

    public void sendPaymentMessage(String key, String message) {
        kafkaProducer.send("payment-topic", key, message, kafkaMessageHelper.getCallback("payment-topic", key, message));
    }
}