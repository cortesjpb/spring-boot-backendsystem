package com.benjacortes.backendsystem;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@SpringBootApplication
public class BackendsystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendsystemApplication.class, args);
	}

	@Bean
	NewTopic createPaymentTopic() {
		return TopicBuilder.name("createPayment").partitions(3).replicas(2).build();
	}
	@Bean
	NewTopic autorizePaymentTopic() {
		return TopicBuilder.name("authorizePayment").partitions(3).replicas(2).build();
	}

	@Autowired
    private KafkaTemplate<String, String> template;

	
	@KafkaListener(topics = "createPayment", groupId = "kafka-sandbox")
	public void createPaymentListener(String message) {
		System.out.format("TOPICO createPayment: Me llegó el siguiente payment \n %s\n\n", message);
		template.send("authorizePayment", message);

	}
	@KafkaListener(topics = "authorizePayment", groupId = "kafka-sandbox")
	public void authorizePaymentListener(String message) {
		System.out.format("TOPICO authorizePayment: Me llegó el siguiente payment \n %s\n\n", message);
	}
}
