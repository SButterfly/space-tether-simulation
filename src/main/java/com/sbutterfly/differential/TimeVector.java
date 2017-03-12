package com.sbutterfly.differential;

/**
 * Created by Sergei on 29.01.2015.
 * <p>
 * Vector, which has one more field for time.
 */
public class TimeVector extends Vector {

    private double time;

    public TimeVector(double time, double... values) {
        super(values);
        this.time = time;
    }

    public TimeVector(double time, Vector vector) {
        super(vector);
        this.time = time;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    @Override
    public TimeVector clone() {
        return new TimeVector(time, this);
    }

    @Override
    public String toString() {
        return "TimeVector{" +
            "time=" + time +
            "} " + super.toString();
    }
}
