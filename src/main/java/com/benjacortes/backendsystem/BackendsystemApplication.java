package com.benjacortes.backendsystem;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;

@SpringBootApplication
public class BackendsystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendsystemApplication.class, args);
	}

	@Bean
	NewTopic hobbit2() {
		return TopicBuilder.name("un-nuevo-topico").partitions(3).replicas(2).build();
	}
}
