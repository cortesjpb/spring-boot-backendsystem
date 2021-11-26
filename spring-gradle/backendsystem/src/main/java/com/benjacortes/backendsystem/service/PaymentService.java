package com.benjacortes.backendsystem.service;

import java.util.List;

import com.benjacortes.backendsystem.data.Payment;
import com.benjacortes.backendsystem.repository.PaymentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }
    
}
