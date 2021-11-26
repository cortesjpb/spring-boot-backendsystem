package com.benjacortes.backendsystem.repository;

import com.benjacortes.backendsystem.data.Payment;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentRepository extends MongoRepository<Payment, String> {}
