package com.sbutterfly.utils;

/**
 * Created by Sergei on 05.03.2015.
 */
public class DoubleUtils {

    private DoubleUtils() {
    }

    public static String toString(double value) {
        String str = Double.toString(value);
        if (str.endsWith(".0")) {
            return str.substring(0, str.length() - 2);
        }
        return str;
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
