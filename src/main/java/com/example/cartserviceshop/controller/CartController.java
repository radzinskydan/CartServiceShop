package com.example.cartserviceshop.controller;

import com.example.cartserviceshop.model.Cart;
import com.example.cartserviceshop.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    
    private final CartService cartService;
    
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }
    
    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getOrCreateCart(cartService.getCartByUserId(userId)));
    }
    
    @PostMapping("/{userId}/add")
    public ResponseEntity<Cart> addToCart(
            @PathVariable Long userId,
            @RequestParam Long productId,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(cartService.addProductToCart(userId, productId, quantity));
    }
    
    @DeleteMapping("/{userId}/remove/{productId}")
    public ResponseEntity<Cart> removeFromCart(
            @PathVariable Long userId,
            @PathVariable Long productId) {
        return ResponseEntity.ok(cartService.removeProductFromCart(userId, productId));
    }
    
    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{userId}/total")
    public ResponseEntity<Double> getCartTotal(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.calculateCartTotal(userId));
    }
} 