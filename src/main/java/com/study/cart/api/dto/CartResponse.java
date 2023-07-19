package com.study.cart.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.study.cart.service.dtos.CartOutput;

import java.time.Instant;
import java.util.List;

public record CartResponse(
        @JsonProperty("id") Long id,
        @JsonProperty("total_items") int totalItems,
        @JsonProperty("items") List<ItemsResponse> items,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("updated_at") Instant updatedAt
) {
    public static CartResponse from(final CartOutput output){
        return new CartResponse(
                output.id(),
                output.totalItems(),
                output.items().stream().map(ItemsResponse::from).toList(),
                output.createdAt(),
                output.updatedAt()
        );
    }
}
