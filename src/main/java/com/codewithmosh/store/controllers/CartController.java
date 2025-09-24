package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.*;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.CartItem;
import com.codewithmosh.store.exceptions.CartItemNotFoundException;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.ProductNotFoundException;
import com.codewithmosh.store.mappers.CartMapper;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import com.codewithmosh.store.repositories.UserRepository;
import com.codewithmosh.store.services.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/carts")
@Tag(name= "Carts")
public class CartController {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;
    private final CartService cartService;
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<CartDto> createCart(UriComponentsBuilder builder) {
        var cartDto = cartService.createCart();
        var uri = builder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();
      return  ResponseEntity.created(uri).body(cartDto);

    }
    @PostMapping("/{cartsID}/items")
    @Operation(summary = "Adds to cart.")
    public ResponseEntity<CartItemDto> addProductToCart(@Parameter(description = "The Id of the cart")
            @PathVariable UUID cartsID, @RequestBody AddItemToCartRequest request) {
     var cartItemDto= cartService.addToCart(cartsID, request.getProductId());
        return  ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }
    @GetMapping ("/{cartsId}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID cartsId) {
        var cartDto = cartService.getCart(cartsId);
    return  ResponseEntity.ok(cartDto);

    }
    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> updateCartItem(@PathVariable("cartId") UUID cartId,
                                                      @PathVariable("productId") Long productId,
                                                      @Valid @RequestBody UpdateCartItemRequest request ) {
        var cart = cartRepository.findById(cartId).orElseThrow(null);
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","cart not found."));
        }
        cartService.updateCart(cart,productId,request);

        return ResponseEntity.ok(cartMapper.toCartDto(cart));
}

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> removeItem(@PathVariable("cartId") UUID cartsId, @PathVariable("productId") Long productId) {
       var cartDto = cartService.removeItem(cartsId,productId);
       return ResponseEntity.ok(cartDto);
    }
    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<Void> clearCart(@PathVariable UUID cartId) {
       cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }
    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String,String>> handlerCartNotFound() {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","cart not found."));
    }
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String,String>> handlerProductNotFound() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","product not found."));
    }
    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<Map<String,String>> handlerCartItemNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","cart item not found."));
    }

}
