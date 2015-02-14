package com.sbutterfly.differential;

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
        TimeVector[] result = new TimeVector[numberOfIterations];
        int i = 0;
        for (TimeVector x : this){
            result[i++] = x;
        }
        return result;
    }

    @Override
    public Iterator<TimeVector> iterator() {
        return new DifferentialIterator();
    }

    public class DifferentialIterator implements Iterator<TimeVector> {
        private Vector last = startVector;
        private double currentTime = startVector.getTime();
        private double h = time / numberOfIterations;
        private int i = 0;

        public boolean hasNext() {
            return i < numberOfIterations;
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
    }
}
