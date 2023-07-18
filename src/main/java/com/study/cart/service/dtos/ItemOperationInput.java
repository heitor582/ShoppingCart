package com.study.cart.service.dtos;

public record ItemOperationInput(
        Long cartId,
        int itemId,
        int quantity
) {
    public static ItemOperationInput with(final Long cartId, final int itemId, final int quantity) {
        return new ItemOperationInput(cartId, itemId, quantity);
    }
}
