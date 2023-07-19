package com.study.cart.service.dtos;

import com.study.cart.entities.Item;
import com.study.cart.utils.DoubleUtils;

public record ItemOutput(
        String name,
        double price,
        int quantity,
        double total

) {
    public static ItemOutput from(final Item item){
        return new ItemOutput(
                item.getName(),
                DoubleUtils.roundToTwoDecimalPlaces(item.getPrice()),
                item.getQuantity(),
                item.getTotalOf()
        );
    }
}
