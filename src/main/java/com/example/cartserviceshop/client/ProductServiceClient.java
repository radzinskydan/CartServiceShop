package com.example.cartserviceshop.client;

import com.example.cartserviceshop.model.CartItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@FeignClient(name = "product-service", url = "${service.product.url}")
public interface ProductServiceClient {
    @GetMapping("/api/products/getById/{productId}")
    CartItem getProductById(@PathVariable Long productId);
    @GetMapping("/api/products/getAll")
    List<CartItem> getAllProducts();
} 