package com.example.cartserviceshop.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "user-service", url = "${service.user.url}")
public interface UserServiceClient {
    @GetMapping("/api/users/getById/{userId}")
    Long getUserById(@PathVariable Long userId);
} 