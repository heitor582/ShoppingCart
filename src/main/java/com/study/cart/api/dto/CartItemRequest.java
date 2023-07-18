package com.study.cart.api.dto;

public record CartItemRequest(
        int itemId,
        int quantity
) {
}
