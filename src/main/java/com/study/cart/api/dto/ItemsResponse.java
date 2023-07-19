package com.study.cart.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.study.cart.service.dtos.ItemOutput;

record ItemsResponse(
        @JsonProperty("name") String name,
        @JsonProperty("price") double price,
        @JsonProperty("quantity") int quantity,
        @JsonProperty("total") double total
){
    public static ItemsResponse from(final ItemOutput output){
        return new ItemsResponse(
                output.name(),
                output.price(),
                output.quantity(),
                output.total()
        );
    }
}