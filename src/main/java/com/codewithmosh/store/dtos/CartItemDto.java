package com.codewithmosh.store.dtos;

import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.Product;
import lombok.Data;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Data
public class CartItemDto {
    private CartProductDto product;
    private Integer quantity;

    private BigDecimal totalPrice;
}
