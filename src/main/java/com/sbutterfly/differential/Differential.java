package com.sbutterfly.differential;

import com.sbutterfly.utils.Func;
import com.sbutterfly.utils.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by Sergei on 03.11.14.
 */
public class Differential {

    private final ODEMethod method;
    private final Function function;
    private final TimeVector startVector;
    private final double totalTime;
    private final int numberOfIterations;

    public Differential(Function function, TimeVector startVector, double totalTime,
                        int numberOfIterations, ODEMethod method) {
        this.function = function;
        this.startVector = startVector;
        this.totalTime = totalTime;
        this.numberOfIterations = numberOfIterations;
        this.method = method;
    }

    public DifferentialResult different() {
        List<TimeVector> result = makeDifferential();
        return new DifferentialResult(result);
    }

    private List<TimeVector> makeDifferential() {
        try (Log.LogTime logTime = Log.recordWorking(this)) {
            List<TimeVector> vectors = new ArrayList<>(numberOfIterations);
            Iterator<TimeVector> iterator = new DifferentialIterator();
            iterator.forEachRemaining(tv -> {
                if (tv.isAnyNaN()) {
                    Log.warning(this, "Some param is NaN is null; throw!!");
                    throw new RuntimeException("Одно или несколько значения стало NaN. " +
                            "Проверьте параметры метода численного интегрирования.");
                }
                vectors.add(tv);
            });
            return vectors;
        }
    }

    @SuppressWarnings("magicnumber")
    public class DifferentialIterator implements Iterator<TimeVector> {
        private Vector last = startVector;
        private double currentTime = startVector.getTime();
        private double h = totalTime / numberOfIterations;

        private Func<Boolean, TimeVector> exitFunc;

        public DifferentialIterator() {
            this.exitFunc = tv -> tv.getTime() + 1e-6 >= totalTime;
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
    }
}
