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
public class PaymentController {

    @Autowired
    PaymentProducerExample paymentProducerExample;

    @GetMapping("/payment")
    public void sendMessage() {

         paymentProducerExample.sendPaymentMessage("payment", "payment");

    }

}
