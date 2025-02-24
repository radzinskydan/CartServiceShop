package com.example.cartserviceshop.model;

import lombok.Data;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Храним только ID пользователя
    @Column(name = "user_id", unique = true)
    private Long userId;
    
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();
} 