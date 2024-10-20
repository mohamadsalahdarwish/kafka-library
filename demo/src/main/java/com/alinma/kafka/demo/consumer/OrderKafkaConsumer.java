package com.alinma.kafka.demo.consumer;

import com.alinma.rib.kafka.order.avro.model.Product;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderKafkaConsumer {

    @KafkaListener(
            topics = "${kafka-consumer-config.consumer-groups.order.topic-id}",
            groupId = "${kafka-consumer-config.consumer-groups.order.consumer-group-id}",
            containerFactory = "orderKafkaListenerContainerFactory"
    )
    public void receiveMessages(
            List<Product> messages,
            @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys
    ) {
        for (int i = 0; i < messages.size(); i++) {
            Product product = messages.get(i);
            String key = keys.get(i);
            System.out.printf("Received order message: %s%n", product.getQuantity());
            System.out.printf("Received order message with Key: %s%n", key);
        }
    }
}
