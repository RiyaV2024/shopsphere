package com.riya.shopsphere.service;
import com.riya.shopsphere.entity.Cart;
import com.riya.shopsphere.entity.CartItem;
import com.riya.shopsphere.entity.Product;
import com.riya.shopsphere.entity.User;
import com.riya.shopsphere.exception.ResourceNotFoundException;
import com.riya.shopsphere.repository.CartRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductService productService;

    public CartService(CartRepository cartRepository, ProductService productService) {
        this.cartRepository = cartRepository;
        this.productService = productService;
    }
    public Cart getCart(User user) {
        Cart cart = cartRepository.findByUser(user);
        if (cart == null) {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setItems(new ArrayList<>());
            return cartRepository.save(newCart);
        }
        return cart;
    }
    public Cart addToCart(User user, Long productId, Integer quantity) {
        Cart cart = getCart(user);
        Product product = productService.getProductById(productId);
        CartItem existingItem = null;
        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getId().equals(productId)) {
                existingItem = item;
                break;
            }
        }
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setCart(cart);
            cart.getItems().add(newItem);
        }

        return cartRepository.save(cart);
    }

    public Cart removeFromCart(User user, Long productId) {
        Cart cart = getCart(user);
        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        return cartRepository.save(cart);
    }
    public Cart updateQuantity(User user, Long productId, Integer quantity) {
        Cart cart = getCart(user);
        boolean found = false;
        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(quantity);
                found = true;
                break;
            }
        }
        if (!found) {
            throw new ResourceNotFoundException("Product not found in cart");
        }
        return cartRepository.save(cart);
    }
    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }
}