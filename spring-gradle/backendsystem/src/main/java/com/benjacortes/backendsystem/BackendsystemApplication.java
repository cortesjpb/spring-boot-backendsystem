package com.benjacortes.backendsystem;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.benjacortes.backendsystem.data.Payment;
import com.benjacortes.backendsystem.data.PaymentStatus;
import com.benjacortes.backendsystem.service.PaymentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;

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
	@Bean
	NewTopic flinkInputTopic() {
		return TopicBuilder.name("flink-input").partitions(3).replicas(2).build();
	}
	@Bean
	NewTopic flinkOutputTopic() {
		return TopicBuilder.name("flink-output").partitions(3).replicas(2).build();
	}

	@Autowired
    private KafkaTemplate<String, String> producerCreatePayment;

	@Autowired
    private KafkaTemplate<String, String> producerAuthorizePayment;

	@Autowired
    private KafkaTemplate<String, String> producerSendedAuthorizations;

	@Autowired
    private KafkaTemplate<String, String> producerAuthorizedPayment;

	@Autowired
    private KafkaTemplate<String, String> producerCanceledPayment;


	@Autowired
	private PaymentService paymentService;

	@Autowired
	private ObjectMapper objectMapper;


	@KafkaListener(topics = "createPayment", groupId = "kafka-sandbox")
	public void createPaymentListener(ConsumerRecord<String, String> record) {
		System.out.format("TOPICO createPayment: Me llegó el siguiente payment \n %s\n\n", record.value());
		System.out.format("TOPICO createPayment: La key del payment es: %s\n\n", record.key());
		try {
			Payment toAuthorize = objectMapper.readValue(record.value(), Payment.class);
			// List<PaymentStatus> status = Arrays.asList(PaymentStatus.CANCELED,PaymentStatus.PROCESSING);
			// Random rand = new Random();
			// toAuthorize.setPaymentStatus(status.get(rand.nextInt(2)).getValue());
			// toAuthorize.setPaymentStatus(PaymentStatus.PROCESSING.getValue());
			Payment finalPayment = paymentService.save(toAuthorize);
			String finalString = objectMapper.writeValueAsString(finalPayment);
			producerCreatePayment.send("authorizePayment", finalPayment.getId(), finalString);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	@KafkaListener(topics = "authorizePayment", groupId = "kafka-sandbox")
	public void authorizePaymentListener(ConsumerRecord<String, String> message) {
		System.out.format("TOPICO authorizePayment: Me llegó el siguiente payment \n %s\n\n", message.value());
		System.out.format("TOPICO authorizePayment: La key del payment es: %s\n\n", message.key());
		try {
			Payment toAuthorize = objectMapper.readValue(message.value(), Payment.class);
			toAuthorize.setPaymentStatus(PaymentStatus.PROCESSING.getValue());
			Payment finalPayment = paymentService.save(toAuthorize);
			String finalString = objectMapper.writeValueAsString(finalPayment);
			producerAuthorizePayment.send("sendedAuthorizations", finalPayment.getId(), finalString);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	@KafkaListener(topics = "sendedAuthorizations", groupId = "kafka-sandbox")
	public void sendedAuthorizationsPaymentListener(ConsumerRecord<String, String> message) {
		System.out.format("TOPICO sendedAuthorizations: Me llegó el siguiente payment \n %s\n\n", message.value());
		System.out.format("TOPICO sendedAuthorizations: La key del payment es: %s\n\n", message.key());
		try {
			Payment toAuthorize = objectMapper.readValue(message.value(), Payment.class);
			List<PaymentStatus> status = Arrays.asList(PaymentStatus.CANCELED,PaymentStatus.ACCEPTED);
			Random rand = new Random();
			toAuthorize.setPaymentStatus(status.get(rand.nextInt(2)).getValue());
			Payment finalPayment = paymentService.save(toAuthorize);
			String finalString = objectMapper.writeValueAsString(finalPayment);
			if (toAuthorize.getPaymentStatus() == PaymentStatus.CANCELED.getValue()) {
				producerSendedAuthorizations.send("canceledPayment", finalPayment.getId(), finalString);
			} else {
				producerSendedAuthorizations.send("authorizedPayment", finalPayment.getId(), finalString);
			}
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	@KafkaListener(topics = "authorizedPayment", groupId = "kafka-sandbox")
	public void authorizedPaymentListener(ConsumerRecord<String, String> message) {
		System.out.format("TOPICO authorizedPayment: Me llegó el siguiente payment \n %s\n\n", message.value());
		System.out.format("TOPICO authorizedPayment: La key del payment es: %s\n\n", message.key());
		try {
			Payment toAuthorize = objectMapper.readValue(message.value(), Payment.class);
			Payment finalPayment = paymentService.save(toAuthorize);
			String finalString = objectMapper.writeValueAsString(finalPayment);
			producerAuthorizedPayment.send("processedPayment", finalPayment.getId(), finalString);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	@KafkaListener(topics = "canceledPayment", groupId = "kafka-sandbox")
	public void canceledPaymentListener(ConsumerRecord<String, String> message) {
		System.out.format("TOPICO canceledPayment: Me llegó el siguiente payment \n %s\n\n", message.value());
		System.out.format("TOPICO canceledPayment: La key del payment es: %s\n\n", message.key());
		try {
			Payment toAuthorize = objectMapper.readValue(message.value(), Payment.class);
			Payment finalPayment = paymentService.save(toAuthorize);
			String finalString = objectMapper.writeValueAsString(finalPayment);
			producerCanceledPayment.send("processedPayment", finalPayment.getId(), finalString);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	@KafkaListener(topics = "processedPayment", groupId = "kafka-sandbox")
	public void processedPaymentListener(ConsumerRecord<String, String> message) {
		System.out.format("TOPICO processedPayment: Me llegó el siguiente payment \n %s\n\n", message.value());
		System.out.format("TOPICO processedPayment: La key del payment es: %s\n\n", message.key());
		try {
			Payment toAuthorize = objectMapper.readValue(message.value(), Payment.class);
			Payment finalPayment = paymentService.save(toAuthorize);
			System.out.format("TOPICO processedPayment: El estado final del pago es: \n %s\n\n", objectMapper.writeValueAsString(finalPayment));
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	@KafkaListener(topics = "flink-output", groupId = "kafka-sandbox")
	public void flinkOutputListener(ConsumerRecord<String, String> message) {
		System.out.format("TOPICO flink-output: \n %s\n\n", message.value());
	}
}
