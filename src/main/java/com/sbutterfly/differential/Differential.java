package com.sbutterfly.differential;

import com.sbutterfly.gui.AdditionalLineView;
import com.sbutterfly.utils.Func;
import com.sbutterfly.utils.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by Sergei on 03.11.14.
 */
public class Differential implements Iterable<TimeVector> {

    private final ODEMethod method;
    private final Function function;
    private final TimeVector startVector;
    private final double time;
    private final int numberOfIterations;

    public Differential(Function function, TimeVector startVector, double time, int numberOfIterations) {
        this(function, startVector, time, numberOfIterations, new RungeKuttaODEMethod());
    }

    public Differential(Function function, TimeVector startVector, double time, int numberOfIterations,
                        ODEMethod method) {
        this.function = function;
        this.startVector = startVector;
        this.time = time;
        this.numberOfIterations = numberOfIterations;
        this.method = method;
    }

    public ODEMethod getMethod() {
        return method;
    }

    public Function getFunction() {
        return function;
    }

    public TimeVector getStartVector() {
        return startVector;
    }

    public double getTime() {
        return time;
    }

    public int getNumberOfIterations() {
        return numberOfIterations;
    }

    public TimeVector[] makeDifferential() {
        return makeDifferential(this.iterator());
    }

    public TimeVector[] makeDifferential(Iterator<TimeVector> iterator) {
        try (Log.LogTime logTime = Log.recordWorking(this)) {
            ArrayList<TimeVector> arrayList = new ArrayList<>(numberOfIterations);
            int i = 0;
            for (; iterator.hasNext(); i++) {
                TimeVector vector = iterator.next();
                if (vector.isAnyNaN()) {
                    Log.warning(this, "Some param is NaN is null; throw!!");
                    throw new RuntimeException("Одно или несколько значения стало NaN. " +
                        "Проверьте параметры метода численного интегрирования.");
                }
                arrayList.add(vector);
            }
            TimeVector[] array = new TimeVector[i];
            arrayList.toArray(array);
            return array;
        }
    }

    public TimeVector[] makeDifferential(Func<Boolean, TimeVector> exitFunc) {
        return makeDifferential(new DifferentialIterator(exitFunc));
    }

    @Override
    public DifferentialIterator iterator() {
        return new DifferentialIterator();
    }

    public DifferentialIterator iterator(Func<Boolean, TimeVector> exitFunc) {
        return new DifferentialIterator(exitFunc);
    }

    public class DifferentialIterator implements Iterator<TimeVector> {
        private Vector last = startVector;
        private double currentTime = startVector.getTime();
        private double h = time / numberOfIterations;

        private Func<Boolean, TimeVector> exitFunc;

        public DifferentialIterator() {
            this.exitFunc = arg1 -> arg1.getTime() + 1e-6 >= time;
        }

        public DifferentialIterator(Func<Boolean, TimeVector> exitFunc) {
            this();
            this.exitFunc = arg1 -> exitFunc.invoke(arg1) || arg1.getTime() + 1e-6 >= time;
        }

        public boolean hasNext() {
            return !exitFunc.invoke(new TimeVector(currentTime, last));
        }

        public TimeVector next() throws NoSuchElementException {
            if (hasNext()) {
                last = method.next(function, last, h);
                currentTime += h;
                return new TimeVector(currentTime, last);
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public double getStatusIndicator() {
            return currentTime / time;
        }

        @Override
        public boolean hasEnded() {
            return !hasNext();
        }
    }
}
