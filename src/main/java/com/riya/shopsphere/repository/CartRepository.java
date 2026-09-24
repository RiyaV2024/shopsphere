package com.riya.shopsphere.repository;

import com.riya.shopsphere.entity.Cart;
import com.riya.shopsphere.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUser(User user);
}