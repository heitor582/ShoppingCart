package com.study.cart.service.dtos;

public record CloseCartOutput(
        double totalPrice,
        double discountPrice,
        int totalItems,
        int totalPaidItems
) {

}
