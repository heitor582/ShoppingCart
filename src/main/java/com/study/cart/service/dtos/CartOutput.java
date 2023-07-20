package com.study.cart.service.dtos;

import com.study.cart.entities.Cart;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public record CartOutput(
        Long id,
        int totalItems,
        List<ItemOutput> items,
        Instant createdAt,
        Instant updatedAt
) implements Serializable {
    public static CartOutput from(final Cart cart){
        return new CartOutput(
                cart.getId(),
                cart.getTotalItems(),
                cart.getItems().stream().map(ItemOutput::from).toList(),
                cart.getCreatedAt(),
                cart.getUpdatedAt()
        );
    }
}
