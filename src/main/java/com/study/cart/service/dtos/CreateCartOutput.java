package com.study.cart.service.dtos;

import com.study.cart.entities.Cart;

public record CreateCartOutput(Long id) {
    public static CreateCartOutput from(final Long id){
        return new CreateCartOutput(id);
    }

    public static CreateCartOutput from(final Cart cart){
        return from(cart.getId());
    }
}
