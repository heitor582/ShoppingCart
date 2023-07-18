package com.study.cart.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.study.cart.entities.Item;

import java.time.Instant;
import java.util.List;

public record CartResponse(
        @JsonProperty("id") Long id,
        @JsonProperty("total_items") int totalItems,
        @JsonProperty("items") List<Item> items,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("updated_at") Instant updatedAt
) {
}
