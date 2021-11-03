package com.benjacortes.backendsystem.controllers;

import java.util.List;

import com.benjacortes.backendsystem.service.ConsumerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaController {

    @Autowired
    private KafkaTemplate<String, String> template;

    @Autowired
    private ConsumerService myTopicConsumer;


    @GetMapping("/kafka/produce")
    public void produce(@RequestParam String message) {
        template.send("MyTopic", message);
    }

    @GetMapping("/kafka/messages")
    public List<String> getMessages() {
        return myTopicConsumer.getMessages();
    }

}

    
    

