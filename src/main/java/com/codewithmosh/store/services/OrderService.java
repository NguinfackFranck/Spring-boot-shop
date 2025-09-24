package com.codewithmosh.store.services;

import com.codewithmosh.store.dtos.OrderDto;
import com.codewithmosh.store.exceptions.OrderNotFoundException;
import com.codewithmosh.store.mappers.OrderMapper;
import com.codewithmosh.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    private OrderRepository orderRepository;
    private AuthService authService;
    private OrderMapper orderMapper;

    public List<OrderDto> getAllOrders(){
        var user= authService.getCurrentUser();
        var orders =orderRepository.getOrdersByCustomer(user);
        return orders.stream().map(orderMapper::toDto).toList();
    }

    public OrderDto getOrder(Long orderId) {
        var order =orderRepository.getOrderWithItems(orderId).orElseThrow(OrderNotFoundException::new);
        var user =authService.getCurrentUser();
        if (!order.isPlacedBy(user)) {
        throw new AccessDeniedException("Access denied");}
    return  orderMapper.toDto(order);}
}
