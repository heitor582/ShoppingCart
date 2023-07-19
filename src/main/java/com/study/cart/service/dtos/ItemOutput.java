package com.study.cart.service.dtos;

import com.study.cart.entities.Item;
public record ItemOutput(
        String name,
        double price,
        int quantity,
        double total

) {
    public static ItemOutput from(final Item item){
        return new ItemOutput(
                item.getName(),
                item.getPrice(),
                item.getQuantity(),
                item.getTotalOf()
        );
    }
}
