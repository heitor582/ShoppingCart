package com.study.cart.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CartItemRequest(
        @JsonProperty("item_id") int itemId,
        @JsonProperty("quantity") int quantity
) {
}
