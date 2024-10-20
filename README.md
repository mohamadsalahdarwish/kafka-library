# Kafka Consumer and Producer Setup Guide

This guide provides steps to add a new consumer and producer for a Kafka topic (`test-topic`) in a Spring Boot project. It also includes Docker Compose configurations to manage Kafka topics and instructions for configuring Kafka consumer and producer in your code.

## Prerequisites

- Docker and Docker Compose installed
- Kafka broker running on Docker
- Spring Boot project with Kafka dependencies

## Step 1: Modify Docker Compose

Update your `docker-compose.yml` to include the Kafka topics management. The following script deletes existing topics (`payment-topic`, `order-topic`, and `test-topic`) and recreates them with replication and partition settings.

```bash
echo -e 'Deleting kafka topics'
kafka-topics --bootstrap-server kafka-broker-1:9092 --topic payment-topic --delete --if-exists
kafka-topics --bootstrap-server kafka-broker-1:9092 --topic order-topic --delete --if-exists
kafka-topics --bootstrap-server kafka-broker-1:9092 --topic test-topic --delete --if-exists

echo -e 'Creating kafka topics'
kafka-topics --bootstrap-server kafka-broker-1:9092 --create --if-not-exists --topic payment-topic --replication-factor 3 --partitions 3
kafka-topics --bootstrap-server kafka-broker-1:9092 --create --if-not-exists --topic order-topic --replication-factor 3 --partitions 3
kafka-topics --bootstrap-server kafka-broker-1:9092 --create --if-not-exists --topic test-topic --replication-factor 3 --partitions 3
```

This ensures that the necessary topics are available for the consumer and producer.

## Step 2: Add Consumer Configuration

Update your consumer properties by adding a configuration for the new `test-topic`:

```yaml
    test:
      consumer-group-id: test-topic-consumer
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
      concurrency-level: 2
      topic-id: test-topic
```

For Avro serialization, include the following properties:

```yaml
      value-deserializer: io.confluent.kafka.serializers.KafkaAvroDeserializer
      properties:
        value.specific.avro.reader: true
```

## Step 3: Kafka Listener Configuration

In the `KafkaListenerConfig` class, add the following bean to handle the new consumer:

```java
   @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> testKafkaListenerContainerFactory() {
        return kafkaConsumerConfig.kafkaListenerContainerFactory("test", String.class, String.class);
    }
```

## Step 4: Create Consumer Class

Create a new Kafka consumer class to handle the messages from `test-topic`.

### `TestKafkaConsumer.java`

```java
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

```

This consumer listens to `test-topic` and processes the messages received.

## Step 5: Add Producer Configuration

In your Spring Boot application, create a producer class for `test-topic`.

### `TestProducerExample.java`

```java
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
```

This producer sends messages to the `test-topic`.

## Step 6: Build and Run the Project

To apply these changes, follow the steps below:

1. **Build the Project**:
   ```bash
   mvn clean install
   ```

2. **Run Docker Compose**:
   ```bash
   docker-compose up -d
   ```

3. **Run the Spring Boot Application**:
   ```bash
   mvn spring-boot:run
   ```

Now your Kafka setup should be ready, with both the producer and consumer configured for `test-topic`.

---

This documentation covers all steps from topic creation to configuring and implementing the Kafka consumer and producer.
