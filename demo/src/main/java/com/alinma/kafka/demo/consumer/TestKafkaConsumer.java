package com.alinma.kafka.demo.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TestKafkaConsumer {

    @KafkaListener(
            topics = "${kafka-consumer-config.consumer-groups.payment.topic-id}",
            groupId = "${kafka-consumer-config.consumer-groups.test.consumer-group-id}",
            containerFactory = "testKafkaListenerContainerFactory"
    )
    public void receive(List<String> messages, List<String> keys, List<Integer> partitions, List<Long> offsets) {
        for (int i = 0; i < messages.size(); i++) {
            System.out.println("Received Test message: " + messages.get(i) +
                    ", key: " + keys.get(i) +
                    ", partition: " + partitions.get(i) +
                    ", offset: " + offsets.get(i));
        }
    }
}