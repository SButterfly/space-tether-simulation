package com.sbutterfly.differential;

/**
 * Created by Sergei on 29.01.2015.
 *
 * Vector, which has one more field for time.
 */
public class TimeVector extends Vector {

    private double time;

    public TimeVector(int n) {
        this(0, n);
    }

    public TimeVector(double... array) {
        super(array);
        setTime(0);
    }

    public TimeVector(Vector array) {
        this(0, array);
    }

    public TimeVector(double time, int n) {
        super(n);
        setTime(time);
    }

    public TimeVector(double time, Vector array) {
        this(time, array.size());
        for (int i = 0, n = array.size(); i < n; i++){
            set(i, array.get(i));
        }
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    @Override
    public TimeVector clone() {
        TimeVector vector = new TimeVector(time, size());
        for (int i = 0, n = size(); i < n; i++){
            vector.set(i, get(i));
        }
        return vector;
    }
}
