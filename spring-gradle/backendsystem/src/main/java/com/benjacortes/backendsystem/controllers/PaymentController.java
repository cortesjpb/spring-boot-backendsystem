package com.benjacortes.backendsystem.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.benjacortes.backendsystem.data.Payment;
import com.benjacortes.backendsystem.service.PaymentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.javafaker.Faker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private KafkaTemplate<String, String> template;

    @Autowired
    private PaymentService paymentService;

    private static Faker faker = new Faker();

    
    private ObjectWriter ow;

    public PaymentController() {
        this.ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }


    @GetMapping
    public List<Payment> findAll() {
        return paymentService.findAll();
    }
    
    @PostMapping("/createPayment")
    public Payment createPayment(@RequestBody Payment payment) {

        Payment newPayment = paymentService.save(payment);
        // template.send("createPayment", payment.toString());
        try {
            template.send("createPayment", newPayment.getId(), ow.writeValueAsString(newPayment));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return newPayment;
    }

    @GetMapping("/createRandomPayment")
    public Payment createPayment() {

        List<String> card = Arrays.asList("Visa Credito", "Visa Debito", "Mastercard Credito", "Mastercard Debito", "AMEX Credito", "AMEX Debito");
        Random rand = new Random();

        Payment payment = new Payment(
                card.get(rand.nextInt(6)),
                faker.book().title() + ", " + faker.book().publisher() + ", " + faker.book().publisher(),
                faker.number().randomDouble(2, 500, 5000)
            );

        System.out.println(payment.toString());
        Payment newPayment = paymentService.save(payment);
        // template.send("createPayment", payment.toString());
        try {
            template.send("createPayment", newPayment.getId(), ow.writeValueAsString(newPayment));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return newPayment;
    }

}
