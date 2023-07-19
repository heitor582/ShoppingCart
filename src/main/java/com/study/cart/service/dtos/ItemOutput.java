package com.study.cart.service.dtos;

import com.study.cart.entities.Item;

import java.io.Serializable;

public record ItemOutput(
        String name,
        double price,
        int quantity,
        double total

)  implements Serializable {
    public static ItemOutput from(final Item item){
        return new ItemOutput(
                item.getName(),
                item.getPrice(),
                item.getQuantity(),
                item.getTotalOf()
        );
    }
}
