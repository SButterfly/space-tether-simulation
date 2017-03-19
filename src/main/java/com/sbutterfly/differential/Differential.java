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
    private final double h;
    private final Func<Boolean, TimeVector> exitFunc;

    public Differential(Function function, TimeVector startVector, double h,
                        ODEMethod method, Func<Boolean, TimeVector> exitFunc) {
        this.function = function;
        this.startVector = startVector;
        this.h = h;
        this.method = method;
        this.exitFunc = exitFunc;
    }

    public DifferentialResult different() {
        List<TimeVector> result = makeDifferential();
        return new DifferentialResult(result);
    }

    private List<TimeVector> makeDifferential() {
        try (Log.LogTime logTime = Log.recordWorking(this)) {
            List<TimeVector> vectors = new ArrayList<>();
            Iterator<TimeVector> iterator = new DifferentialIterator(exitFunc);
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
        private TimeVector last = startVector;

        private Func<Boolean, TimeVector> exitFunc;

        public DifferentialIterator(Func<Boolean, TimeVector> exitFunc) {
            this.exitFunc = exitFunc;
        }

        public boolean hasNext() {
            return !exitFunc.invoke(last);
        }

        public TimeVector next() throws NoSuchElementException {
            if (hasNext()) {
                TimeVector current = last;
                last = nextValue(current);
                return last;
            } else {
                throw new NoSuchElementException();
            }
        }

        private TimeVector nextValue(TimeVector currentVector) {
            Vector vector = method.next(function, currentVector, h);
            return new TimeVector(currentVector.getTime() + h, vector);
        }
    }
}
