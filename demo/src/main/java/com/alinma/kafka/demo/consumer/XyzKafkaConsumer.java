package com.alinma.kafka.demo.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class XyzKafkaConsumer {

    @KafkaListener(
            topics = "${kafka-consumer-config.consumer-groups.xyz.topic-id}",
            groupId = "${kafka-consumer-config.consumer-groups.xyz.consumer-group-id}",
            containerFactory = "xyzKafkaListenerContainerFactory"
    )
    public void receive(List<String> messages, List<String> keys) {
        for (int i = 0; i < messages.size(); i++) {
            System.out.println("Received Test message: " + messages.get(i) +
                    ", key: " + keys.get(i));
        }
    }
}