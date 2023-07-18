package com.study.cart.service.dtos;

import com.study.cart.entities.Cart;

public record ListCartOutput(
        Long id,
        int totalItems
) {
    public static ListCartOutput from(final Cart cart){
        return new ListCartOutput(cart.getId(), cart.getTotalItems());
    }
}
