package com.riya.shopsphere.repository;

import com.riya.shopsphere.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
