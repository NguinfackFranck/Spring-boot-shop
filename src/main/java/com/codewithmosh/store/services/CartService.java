package com.codewithmosh.store.services;

import com.codewithmosh.store.dtos.CartDto;
import com.codewithmosh.store.dtos.CartItemDto;
import com.codewithmosh.store.dtos.UpdateCartItemRequest;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.CartItem;
import com.codewithmosh.store.exceptions.CartItemNotFoundException;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.ProductNotFoundException;
import com.codewithmosh.store.mappers.CartMapper;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor

public class CartService {
    private CartRepository cartRepository;
    private CartMapper cartMapper;
    private ProductRepository productRepository;

    public CartDto createCart() {
        var cart= new Cart();
        cartRepository.save(cart);
        return cartMapper.toCartDto(cart);
    }
    public CartItemDto addToCart(UUID cartsID, Long productId) {
        var cart = cartRepository.findById(cartsID).orElse(null);

        if (cart == null) {
            throw new CartNotFoundException("Cart not found");
        }
        var product = productRepository.findById(productId).orElse(null);
        if (product==null) {
            throw new ProductNotFoundException();
        }
        var cartItem = cart.getItem(productId);
        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() +1);
        }
        else  {
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItem.setCart(cart);
            cart.getItems().add(cartItem);
        }
        cartRepository.save(cart);
        return  cartMapper.toCartItemDto(cartItem);
    }
    public void updateCart(Cart cart, Long productId, UpdateCartItemRequest request) {
        var cartItem = cart.getItem(productId);
        if (cartItem == null) {
           throw new CartItemNotFoundException();
        }
        cartItem.setQuantity(request.getQuantity());
        cartRepository.save(cart);
    }
    public CartDto getCart(UUID cartsId) {
        var cart = cartRepository.findById(cartsId).orElse(null);
        if (cart == null) {
            throw  new CartNotFoundException("Cart not found");
        }
        return  cartMapper.toCartDto(cart);
    }
    public CartDto removeItem(UUID cartsId, Long productId) {
        var cart = cartRepository.getCartWithItems(cartsId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException("Cart not found");
        }
        cart.removeItem(productId);
        cartRepository.save(cart);
        return cartMapper.toCartDto(cart);
    }
    public void clearCart(UUID cartId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException("Cart not found");
        }
        cart.clearCart();
        cartRepository.save(cart);
    }
}
