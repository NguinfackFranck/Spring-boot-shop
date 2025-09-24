package com.codewithmosh.store.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @ManyToOne()
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "customer_id")
    private User customer;


    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;


    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;


    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @OneToMany( mappedBy = "order", cascade = {CascadeType.PERSIST,CascadeType.REMOVE},orphanRemoval = true)

    private Set<OrdersItem> Items = new LinkedHashSet<>();
    public static Order fromCart(Cart cart, User customer){
        var order = new Order();
        order.setCustomer(customer);
        order.setStatus(PaymentStatus.PENDING);
        order.setTotalPrice(cart.getTotalPrice());
        cart.getItems().forEach(item -> {
            var orderItem= new OrdersItem();
            orderItem.setOrder(order);
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setTotalPrice(item.getTotalPrice());
            orderItem.setUnitPrice(item.getProduct().getPrice());
            order.getItems().add(orderItem);
        });
   return order; }
    public boolean isPlacedBy(User customer){
     return this.customer.equals(customer);
    }

}