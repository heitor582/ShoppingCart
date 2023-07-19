package com.study.cart.utils;

import java.text.DecimalFormat;

public final class DoubleUtils {
    private DoubleUtils() {
    }

    public static double roundToTwoDecimalPlaces(final double value){
        final DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return Double.parseDouble(decimalFormat.format(value));
    }
}
