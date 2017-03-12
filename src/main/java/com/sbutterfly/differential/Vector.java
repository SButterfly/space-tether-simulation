package com.sbutterfly.differential;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by Sergei on 03.11.14.
 */
public class Vector implements Iterable<Double> {

    private final double[] values;

    public Vector(int length) {
        values = new double[length];
    }

    public Vector(double... values) {
        this.values = values;
    }

    public Vector(Vector vector) {
        this.values = vector.values.clone();
    }

    public int size() {
        return values.length;
    }

    public double get(int index) {
        return values[index];
    }

    public void set(int index, double value) {
        values[index] = value;
    }

    public boolean isAllNaN() {
        for (double a : values) {
            if (!Double.isNaN(a)) {
                return false;
            }
        }
        return true;
    }

    public boolean isAnyNaN() {
        for (double a : values) {
            if (Double.isNaN(a)) {
                return true;
            }
        }
        return false;
    }

    public static Vector sum(Vector x, Vector y) {
        if (x.size() != y.size()) {
            throw new ArrayIndexOutOfBoundsException("Вектора должны быть одной длины");
        }

        Vector result = new Vector(x.size());

        for (int i = 0, n = x.size(); i < n; i++) {
            result.values[i] = x.values[i] + y.values[i];
        }

        return result;
    }

    public static Vector sum(double x, Vector y) {
        return sum(y, x);
    }

    public static Vector sum(Vector x, double y) {
        Vector result = new Vector(x.size());

        for (int i = 0, n = x.size(); i < n; i++) {
            result.values[i] = x.values[i] + y;
        }

        return result;
    }

    public static Vector sum(Vector... v) {
        if (v.length == 0) {
            throw new IllegalArgumentException("Пустой массив");
        }
        if (v.length == 1) {
            return new Vector(v[0]);
        }

        Vector result = new Vector(v[0].size());

        for (Vector x : v) {
            for (int i = 0, n = x.size(); i < n; i++) {
                result.values[i] += x.values[i];
            }
        }
        return result;
    }

    public static Vector mul(double y, Vector x) {
        Vector result = new Vector(x.size());

        for (int i = 0, n = x.size(); i < n; i++) {
            result.values[i] = x.values[i] * y;
        }

        return result;
    }

    public static Vector mul(Vector x, double y) {
        return mul(y, x);
    }

    public static Vector mulThenSum(Vector x, double mul, Vector sum) {
        Vector result = new Vector(x.size());

        for (int i = 0, n = x.size(); i < n; i++) {
            result.values[i] = x.values[i] * mul + sum.values[i];
        }

        return result;
    }

    @Override
    public String toString() {
        return Arrays.toString(values);
    }

    @Override
    public Iterator<Double> iterator() {
        return new Iterator<Double>() {

            int currentIndex;

            @Override
            public boolean hasNext() {
                return currentIndex < size();
            }

            @Override
            public Double next() {
                if (hasNext()) {
                    return get(currentIndex++);
                } else {
                    throw new NoSuchElementException();
                }
            }
        };
    }
}
