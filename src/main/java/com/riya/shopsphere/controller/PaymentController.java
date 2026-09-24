package com.riya.shopsphere.controller;

import com.riya.shopsphere.entity.Payment;
import com.riya.shopsphere.service.PaymentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/pay/{orderId}")
    public Payment processPayment(@PathVariable Long orderId) {
        return paymentService.processPayment(orderId);
    }
}