package com.example.cartserviceshop.model;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;
    
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_price")
    private double productPrice;
    
    private Integer quantity;
} 