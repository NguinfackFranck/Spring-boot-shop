package com.codewithmosh.store.repositories;

import com.codewithmosh.store.entities.Cart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends CrudRepository<Cart, UUID> {

    @EntityGraph(attributePaths = "items.product")
    @Query("select c from Cart c WHERE c.id= :cartId")
    Optional<Cart> getCartWithItems(@Param("cartId") UUID cartId);
   }
