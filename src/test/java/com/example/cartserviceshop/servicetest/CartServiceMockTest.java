package com.example.cartserviceshop.servicetest;

import com.example.cartserviceshop.client.UserServiceClient;
import com.example.cartserviceshop.controller.CartController;
import com.example.cartserviceshop.model.Cart;
import com.example.cartserviceshop.repository.CartRepository;
import com.example.cartserviceshop.service.CartService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CartServiceMockTest {

    @MockitoBean
    private UserServiceClient userServiceClient;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartService cartService;

    @Autowired
    private CartController cartController; // Инжектим контроллер для тестирования

    @Test
    public void testGetCartByUserId() {
        // Given
        Long userId = 123L;
        Long expectedCartId = 456L;

        // Define mock behavior: userServiceClient.getUserById(userId) will return expectedCartId
        when(userServiceClient.getUserById(userId)).thenReturn(expectedCartId);

        // When
        Long actualCartId = cartService.getCartByUserId(userId);

        // Then
        assertEquals(expectedCartId, actualCartId);
    }

    @Test
    public void testGetOrCreateCartExistingCart() {
        // Given
        Long userId = 123L;
        Cart existingCart = new Cart();
        existingCart.setUserId(userId);
        existingCart.setId(789L);

        // Define mock behavior: cartRepository.findByUserId(userId) will return existingCart wrapped in Optional
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(existingCart));

        // When
        Cart actualCart = cartService.getOrCreateCart(userId);

        // Then
        assertEquals(existingCart.getId(), actualCart.getId());
        assertEquals(userId, actualCart.getUserId());
    }

    @Test
    public void testGetOrCreateCartNewCart() {
        // Given
        Long userId = 123L;
        Cart newCart = new Cart();
        newCart.setUserId(userId);
        newCart.setId(789L);

        // Define mock behavior:
        // 1. cartRepository.findByUserId(userId) will return empty Optional (no cart exists)
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());
        // 2. cartRepository.save(any(Cart.class)) will return the newCart (simulating saving the new cart to the database)
        when(cartRepository.save(Mockito.any(Cart.class))).thenReturn(newCart);

        // When
        Cart actualCart = cartService.getOrCreateCart(userId);

        // Then
        assertEquals(newCart.getId(), actualCart.getId());
        assertEquals(userId, actualCart.getUserId());
    }

    // Пример теста для контроллера, который использует мокнутый сервис
    @Test
    public void testGetCartController() {
        // Given
        Long userId = 123L;
        Cart expectedCart = new Cart();
        expectedCart.setUserId(userId);
        expectedCart.setId(456L);

        // Настраиваем моки для сервиса
        when(userServiceClient.getUserById(userId)).thenReturn(userId); // Мокаем getUserById
        when(cartService.getOrCreateCart(userId)).thenReturn(expectedCart); // Мокаем getOrCreateCart

        // When
        ResponseEntity<Cart> response = cartController.getCart(userId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedCart.getId(), response.getBody().getId());
        assertEquals(userId, response.getBody().getUserId());
    }
}

