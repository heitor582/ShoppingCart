package com.study.cart.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.study.cart.service.dtos.ListCartOutput;

import java.time.Instant;

public record CartsListResponse(
        @JsonProperty("id") Long id,
        @JsonProperty("total_items") int totalItems,
        @JsonProperty("created_at") Instant createdAt
) {
    public static CartsListResponse from(final ListCartOutput output){
        return new CartsListResponse(
                output.id(),
                output.totalItems(),
                output.createdAt()
        );
    }
}
