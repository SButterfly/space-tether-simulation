package com.sbutterfly.differential;

import com.sbutterfly.GUI.AdditionalLineView;
import com.sbutterfly.utils.Log;

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

    public Differential(Function function, TimeVector startVector, double time, int numberOfIterations, ODEMethod method){
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

    public TimeVector[] makeDifferential(){
        return makeDifferential(this.iterator());
    }

    public TimeVector[] makeDifferential(Iterator<TimeVector> iterator) {
        try (Log.LogTime logTime = Log.recordWorking(this)) {
            TimeVector[] result = new TimeVector[numberOfIterations];
            for (int i = 0; iterator.hasNext(); i++) {
                result[i] = iterator.next();
            }
            return result;
        }
    }

    @Override
    public DifferentialIterator iterator() {
        return new DifferentialIterator();
    }

    public class DifferentialIterator implements Iterator<TimeVector>, AdditionalLineView.Processable {
        private Vector last = startVector;
        private double currentTime = startVector.getTime();
        private double h = time / numberOfIterations;
        private int i = 0;

        private boolean canceled = false;

        public boolean hasNext() {
            return !canceled && i < numberOfIterations;
        }

        public TimeVector next() throws NoSuchElementException {
            if (hasNext()){
                last = method.Next(function, last, h);
                currentTime += h;
                i++;
                return new TimeVector(currentTime, last);
            }
            else
                throw new NoSuchElementException();
        }

        @Override
        public double getStatusIndicator() {
            return currentTime / time;
        }

        @Override
        public boolean hasEnded() {
            return !hasNext();
        }

        @Override
        public boolean hasCanceled() {
            return canceled;
        }

        @Override
        public void cancel() {
            canceled = true;
        }
    }
}
