package com.riya.shopsphere.controller;

import com.riya.shopsphere.entity.Order;
import com.riya.shopsphere.entity.User;
import com.riya.shopsphere.repository.UserRepository;
import com.riya.shopsphere.service.OrderService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;

    public OrderController(OrderService orderService, UserRepository userRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    private User getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email);
    }

    @PostMapping("/place")
    public Order placeOrder(Authentication authentication) {
        User user = getCurrentUser(authentication);
        return orderService.placeOrder(user);
    }

    @GetMapping("/history")
    public List<Order> getOrderHistory(Authentication authentication) {
        User user = getCurrentUser(authentication);
        return orderService.getOrderHistory(user);
    }
}