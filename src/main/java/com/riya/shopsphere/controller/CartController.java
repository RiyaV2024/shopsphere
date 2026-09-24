package com.riya.shopsphere.controller;
import com.riya.shopsphere.entity.Cart;
import com.riya.shopsphere.entity.User;
import com.riya.shopsphere.repository.UserRepository;
import com.riya.shopsphere.service.CartService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    private final UserRepository userRepository;
    public CartController(CartService cartService, UserRepository userRepository) {
        this.cartService = cartService;
        this.userRepository = userRepository;
    }

    private User getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email);
    }

    @GetMapping
    public Cart getCart(Authentication authentication) {
        User user = getCurrentUser(authentication);
        return cartService.getCart(user);
    }

    @PostMapping("/add")
    public Cart addToCart(Authentication authentication,
                          @RequestParam Long productId,
                          @RequestParam Integer quantity) {
        User user = getCurrentUser(authentication);
        return cartService.addToCart(user, productId, quantity);
    }

    @DeleteMapping("/remove/{productId}")
    public Cart removeFromCart(Authentication authentication, @PathVariable Long productId) {
        User user = getCurrentUser(authentication);
        return cartService.removeFromCart(user, productId);
    }

    @PutMapping("/update")
    public Cart updateQuantity(Authentication authentication,
                               @RequestParam Long productId,
                               @RequestParam Integer quantity) {
        User user = getCurrentUser(authentication);
        return cartService.updateQuantity(user, productId, quantity);
    }
}