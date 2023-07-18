package com.study.cart.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record CartsListResponse(
        @JsonProperty("id") Long id,
        @JsonProperty("total_items") int totalItems,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("updated_at") Instant updatedAt
) {
}
