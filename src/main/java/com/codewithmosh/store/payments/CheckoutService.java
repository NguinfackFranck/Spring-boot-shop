package com.codewithmosh.store.payments;

import com.codewithmosh.store.entities.Order;
import com.codewithmosh.store.exceptions.CartEmptyException;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.OrderRepository;
import com.codewithmosh.store.services.AuthService;
import com.codewithmosh.store.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CheckoutService {
    private final CartRepository cartRepository;
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final PaymentGateway paymentGateway;

    @Transactional
    public CheckoutResponse checkout(CheckoutRequest request)  {
        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException("Cart not found");
        }
        if (cart.isEmpty()) {
            throw new CartEmptyException("Cart is empty");
        }
        var order   = Order.fromCart(cart,authService.getCurrentUser());

        orderRepository.save(order);
        // checkout session creation
       try {

           var session = paymentGateway.createCheckoutSession(order);
           cartService.clearCart(cart.getId());
           return new CheckoutResponse(order.getId(),session.getCheckoutUrl());

       }
       catch (PaymentException e) {

           orderRepository.delete(order);
           throw e;
       }
    }
    public void handleWebhook(WebhookRequest request) {
        paymentGateway
                .parseWebhookRequest(request)
                .ifPresent(PaymentResult ->{

                    var order= orderRepository.findById(PaymentResult.getOrderId()).orElseThrow();
                    order.setStatus(PaymentResult.getPaymentStatus());
                    orderRepository.save(order);
                });

    }
}
