package com.benjacortes.backendsystem.controllers;

import java.util.List;
import java.util.Random;

import com.benjacortes.backendsystem.service.ConsumerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.benjacortes.backendsystem.data.ClickEvent;

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

    private ObjectMapper om = new ObjectMapper();


    @GetMapping("/kafka/produce")
    public void produce(@RequestParam String message) {
        template.send("MyTopic", message);
    }

    @GetMapping("/kafka/messages")
    public List<String> getMessages() {
        return myTopicConsumer.getMessages();
    }

    @GetMapping("kafka/flink-input")
    public void createClick(){
        // List<String> clicks = Arrays.asList("SessionId 1, Page 1", "SessionId 2, Page 1", "SessionId 3, Page 1", "SessionId 1, Page 2", "SessionId 2, Page 2", "SessionId 3, Page 2", "SessionId 1, Page 3", "SessionId 2, Page 3", "SessionId 3, Page 3");

        Random rand = new Random();
        ClickEvent event = new ClickEvent(Long.valueOf(rand.nextInt(9)), Long.valueOf(rand.nextInt(9)), Integer.valueOf(1));
        try {
            template.send("flink-input", om.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}

    
    

