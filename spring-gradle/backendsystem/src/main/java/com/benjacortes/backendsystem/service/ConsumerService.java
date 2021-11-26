package com.benjacortes.backendsystem.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ConsumerService {

    private final List<String> messages = new ArrayList<>();

    @KafkaListener(topics = "MyTopic", groupId = "kafka-sandbox")
    public void listen(String message) {
        synchronized (messages) {
            // messages.add(message);
            System.out.println(message);
        }
    }

    public List<String> getMessages() {
        return messages;
    }

}
