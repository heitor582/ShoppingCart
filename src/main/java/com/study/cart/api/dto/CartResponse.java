package com.study.cart.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.study.cart.entities.Item;
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
}
