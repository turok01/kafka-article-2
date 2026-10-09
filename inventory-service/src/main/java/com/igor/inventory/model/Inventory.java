package com.igor.inventory.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "inventory")
@Getter
@Setter
@NoArgsConstructor
public class Inventory {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name", nullable = false, unique = true, length = 256)
    private String productName;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    public Inventory(String productName, Integer quantity) {
        this.productName = productName;
        this.quantity = quantity;
    }

    // empty constructor, getters, setters
}
