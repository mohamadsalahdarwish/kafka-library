package com.alinma.kafka.demo;

import com.alinma.rib.kafka.config.data.KafkaConfigData;
import com.alinma.rib.kafka.config.data.KafkaConsumerConfigData;
import com.alinma.rib.kafka.config.data.KafkaProducerConfigData;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.alinma.kafka.demo.*", "com.alinma.rib.kafka.*"})
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
