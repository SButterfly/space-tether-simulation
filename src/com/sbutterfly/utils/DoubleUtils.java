package com.sbutterfly.utils;

/**
 * Created by Sergei on 05.03.2015.
 */
public class DoubleUtils {
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
}
