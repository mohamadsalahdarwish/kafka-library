package com.alinma.kafka.demo.controller;


import com.alinma.kafka.demo.producer.MsdKafkaProducer;
import com.alinma.kafka.demo.producer.OrderProducerExample;
import com.alinma.kafka.demo.producer.PaymentProducerExample;
import com.alinma.kafka.demo.producer.TestProducerExample;
import com.alinma.rib.kafka.order.avro.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class EventController {

    @Autowired
    PaymentProducerExample paymentProducerExample;

    @Autowired
    OrderProducerExample orderProducerExample;

    @Autowired
    TestProducerExample testProducerExample;

    @Autowired
    MsdKafkaProducer msdKafkaProducer;

    @GetMapping("/events")
    public void sendMessage() {

         paymentProducerExample.sendPaymentMessage("test", "test");

        Product product = new Product("1",100);


        orderProducerExample.sendOrderMessage("test2", product);

        testProducerExample.send("test1", "test1");

        msdKafkaProducer.send("msd", "msd");

    }

}
