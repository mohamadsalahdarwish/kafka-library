// OrderKafkaConsumer.java

package com.alinma.kafka.demo.consumer;

import com.alinma.rib.kafka.order.avro.model.Product;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderKafkaConsumer {

    @KafkaListener(
            topics = "${kafka-consumer-config.consumer-groups.order.topic-id}",//"order-topic",
            groupId = "${kafka-consumer-config.consumer-groups.order.consumer-group-id}",
            containerFactory = "orderKafkaListenerContainerFactory"
    )
    public void receive(List<SpecificRecordBase> messages, List<Product> keys, List<Integer> partitions, List<Long> offsets) {
        for (int i = 0; i < messages.size(); i++) {
            System.out.println("Received order message: " + ((Product)messages.get(i)).getQuantity());
            System.out.println("Received order message with Key: " + keys.get(i).getId());
        }
    }
}
