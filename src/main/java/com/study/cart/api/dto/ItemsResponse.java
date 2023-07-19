package com.study.cart.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.study.cart.entities.Item;

record ItemsResponse(
        @JsonProperty("name") String name,
        @JsonProperty("price") double price,
        @JsonProperty("quantity") int quantity,
        @JsonProperty("total") double total
){
    public static ItemsResponse from(final Item item){
        return new ItemsResponse(
                item.getName(),
                item.getPrice(),
                item.getQuantity(),
                item.getTotalOf()
        );
    }
}