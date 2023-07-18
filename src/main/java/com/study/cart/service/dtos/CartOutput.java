package com.study.cart.service.dtos;

import com.study.cart.entities.Cart;
import com.study.cart.entities.Item;
import java.time.Instant;
import java.util.List;

public record CartOutput(
        Long id,
        int totalItems,
        List<Item> items,
        Instant createdAt,
        Instant updatedAt
){
    public static CartOutput from(final Cart cart){
        return new CartOutput(
                cart.getId(),
                cart.getTotalItems(),
                cart.getItems().stream().toList(),
                cart.getCreatedAt(),
                cart.getUpdatedAt()
        );
    }
}
