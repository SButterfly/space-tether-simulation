package com.sbutterfly.differential;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by Sergei on 03.11.14.
 */
public class Vector implements Iterable<Double> {

    private final double[] values;

    public Vector(double... values) {
        this.values = values;
    }

    protected Vector(Vector vector) {
        this.values = vector.values;
    }

    public int size() {
        return values.length;
    }

    public double get(int index) {
        return values[index];
    }

    public boolean isAnyNaN() {
        for (int i = 0; i < values.length; i++) {
            if (Double.isNaN(values[i])) {
                return true;
            }
        }
        return false;
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

    public double[] toArray() {
        return values.clone();
    }
}
