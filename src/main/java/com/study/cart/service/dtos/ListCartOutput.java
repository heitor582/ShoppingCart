package com.study.cart.service.dtos;

import com.study.cart.entities.Cart;

import java.io.Serializable;
import java.time.Instant;

public record ListCartOutput(
        Long id,
        int totalItems,
        Instant createdAt
) implements Serializable {
    public static ListCartOutput from(final Cart cart){
        return new ListCartOutput(cart.getId(), cart.getTotalItems(), cart.getCreatedAt());
    }
}
