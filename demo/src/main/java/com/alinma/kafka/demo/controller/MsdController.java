package com.alinma.kafka.demo.controller;


import com.alinma.kafka.demo.producer.MsdKafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MsdController {

    @Autowired
    MsdKafkaProducer msdKafkaProducer;

    @GetMapping("/msd")
    public void sendMessage() {

        msdKafkaProducer.send("msd", "msd");

    }

}
