package com.codewithmosh.store.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;


    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "date_created", insertable = false, updatable = false)
    private Instant dateCreated;

    @OneToMany(mappedBy = "cart",cascade = CascadeType.MERGE,orphanRemoval = true, fetch = FetchType.EAGER)

    private Set<CartItem> items = new LinkedHashSet<>();
    public BigDecimal getTotalPrice() {
        return items.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public void removeItem(Long productId) {
        var cartItem = items.stream().filter(i -> i.getProduct().getId().equals(productId)).findFirst().orElse(null);
        if (cartItem != null) {
        items.remove(cartItem);
        }}
    public CartItem getItem(Long productId) {
        return items.stream().filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
    }
        public void addItem(Product product, int quantity) {
            CartItem item = new CartItem();
            item.setCart(this);
            item.setProduct(product);
            item.setQuantity(quantity);
            items.add(item);
        }
        public void clearCart() {
        items.clear();
        }
        public boolean isEmpty() {
            return items.isEmpty();
        }
    }

