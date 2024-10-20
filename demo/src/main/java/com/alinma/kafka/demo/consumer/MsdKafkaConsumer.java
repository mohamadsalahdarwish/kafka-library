package com.alinma.kafka.demo.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MsdKafkaConsumer {

    @KafkaListener(
            topics = "${kafka-consumer-config.consumer-groups.msd.topic-id}",
            groupId = "${kafka-consumer-config.consumer-groups.msd.consumer-group-id}",
            containerFactory = "msdKafkaListenerContainerFactory"
    )
    public void receive(List<String> messages, List<String> keys, List<Integer> partitions, List<Long> offsets) {
        for (int i = 0; i < messages.size(); i++) {
            System.out.println("Received msd message: " + messages.get(i) +
                    ", key: " + keys.get(i) );
        }
    }
}