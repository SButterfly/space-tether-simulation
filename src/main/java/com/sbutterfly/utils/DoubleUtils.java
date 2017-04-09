package com.sbutterfly.utils;

import java.text.DecimalFormat;

/**
 * Created by Sergei on 05.03.2015.
 */
public class DoubleUtils {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.###");

    private DoubleUtils() {
    }

    public static String toString(double value) {
        return DECIMAL_FORMAT.format(value);
    }

    public static double parse(String value) {
        return Double.parseDouble(value);
    }

    public static double nonNegativeParse(String value) {
        double val = DoubleUtils.parse(value);
        if (val >= 0) {
            return val;
        } else {
            throw new NumberFormatException();
        }
    }
}
