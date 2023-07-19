package com.study.cart.service.dtos;

import com.study.cart.utils.DoubleUtils;

public record CloseCartOutput(
        double totalPrice,
        double discountedPrice,
        double discount,
        int totalItems,
        int totalPaidItems
) {
    public static CloseCartOutput from(
            final double totalPrice,
            final double discountedPrice,
            final double discount,
            final int totalItems,
            final int totalPaidItems
    ){
        return new CloseCartOutput(
                DoubleUtils.roundToTwoDecimalPlaces(totalPrice),
                DoubleUtils.roundToTwoDecimalPlaces(discountedPrice),
                DoubleUtils.roundToTwoDecimalPlaces(discount),
                totalItems,
                totalPaidItems
        );
    }
}
