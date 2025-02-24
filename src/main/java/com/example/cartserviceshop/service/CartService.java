package com.example.cartserviceshop.service;

import com.example.cartserviceshop.client.ProductServiceClient;
import com.example.cartserviceshop.client.UserServiceClient;
import com.example.cartserviceshop.model.Cart;
import com.example.cartserviceshop.model.CartItem;
import com.example.cartserviceshop.repository.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CartService {
    
    private final CartRepository cartRepository;
    private final UserServiceClient userServiceClient;
    private final ProductServiceClient productService;

    public CartService(CartRepository cartRepository, UserServiceClient userServiceClient, ProductServiceClient productService) {
        this.cartRepository = cartRepository;
        this.userServiceClient = userServiceClient;
        this.productService = productService;
    }

    public Long getCartByUserId(Long userId) {
        return userServiceClient.getUserById(userId);
    }


    public Cart getOrCreateCart(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    return cartRepository.save(newCart);
                });
    }
    
    public Cart addProductToCart(Long userId, Long productId, Integer quantity) {
        Cart cart = getOrCreateCart(userId);
        CartItem product = productService.getProductById(productId);
        
        // Проверяем, есть ли уже такой продукт в корзине
        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .orElse(null);
                
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setProductId(product.getId());
            newItem.setQuantity(quantity);
            cart.getItems().add(newItem);
        }
        
        return cartRepository.save(cart);
    }
    
    public Cart removeProductFromCart(Long userId, Long productId) {
        Cart cart = getOrCreateCart(userId);
        cart.getItems().removeIf(item -> item.getProductId().equals(productId));
        return cartRepository.save(cart);
    }
    
    public void clearCart(Long userId) {
        Cart cart = getOrCreateCart(userId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }
    
    public Double calculateCartTotal(Long userId) {
        Cart cart = getOrCreateCart(userId);
        return cart.getItems().stream()
                .mapToDouble(item -> item.getProductPrice() * item.getQuantity())
                .sum();
    }
} 