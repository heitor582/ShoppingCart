package com.study.cart.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.study.cart.service.dtos.CloseCartOutput;

public record CloseResponse(
        @JsonProperty("total_price") double totalPrice,
        @JsonProperty("discounted_price") double discountedPrice,
        @JsonProperty("discount") double discount,
        @JsonProperty("total_items") int totalItems,
        @JsonProperty("total_paid_items") int totalPaidItems
) {
    public static CloseResponse from(final CloseCartOutput output) {
        return new CloseResponse(
          output.totalPrice(),
          output.discountedPrice(),
          output.discount(),
          output.totalItems(),
          output.totalPaidItems()
        );
    }
}
