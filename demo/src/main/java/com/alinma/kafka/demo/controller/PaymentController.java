package com.alinma.kafka.demo.controller;

import com.alinma.kafka.demo.producer.PaymentProducerExample;
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
