package com.riya.shopsphere.service;

import com.riya.shopsphere.entity.Order;
import com.riya.shopsphere.entity.OrderStatus;
import com.riya.shopsphere.entity.Payment;
import com.riya.shopsphere.entity.PaymentStatus;
import com.riya.shopsphere.exception.ResourceNotFoundException;
import com.riya.shopsphere.repository.OrderRepository;
import com.riya.shopsphere.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    public Payment processPayment(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaymentDate(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);

        order.setStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);

        return savedPayment;
    }
}