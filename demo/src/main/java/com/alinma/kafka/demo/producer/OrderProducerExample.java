package com.alinma.kafka.demo.producer;


import com.alinma.rib.kafka.producer.KafkaMessageHelper;
import com.alinma.rib.kafka.producer.service.KafkaProducer;
import com.alinma.rib.kafka.producer.service.impl.AvrokafkaProducerImpl;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderProducerExample {

    private final KafkaProducer<String, SpecificRecordBase> kafkaProducer;
    private final KafkaMessageHelper<String, SpecificRecordBase> kafkaMessageHelper;

    @Autowired
    public OrderProducerExample(AvrokafkaProducerImpl<String, SpecificRecordBase> kafkaProducer, KafkaMessageHelper<String, SpecificRecordBase> kafkaMessageHelper) {
        this.kafkaProducer = kafkaProducer;
        this.kafkaMessageHelper = kafkaMessageHelper;
    }

    public void sendOrderMessage(String key, SpecificRecordBase message) {
        kafkaProducer.send("order-topic", key, message, kafkaMessageHelper.getCallback("order-topic", key, message));
    }
}