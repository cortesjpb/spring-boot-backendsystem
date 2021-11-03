package com.benjacortes.backendsystem.controllers;

import java.io.IOException;

import com.benjacortes.backendsystem.data.Payment;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private KafkaTemplate<String, String> template;

    
    private ObjectWriter ow;

    public PaymentController() {
        this.ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }



    @PostMapping("/createPayment")
    public Payment createPayment(@RequestBody Payment payment) {

        try {
        // template.send("createPayment", payment.toString());
        template.send("createPayment", ow.writeValueAsString(payment));
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return payment;
    }

}
