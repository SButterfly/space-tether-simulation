package com.sbutterfly.pendulum;

import com.sbutterfly.differential.*;
import com.sbutterfly.services.AppSettings;

/**
 * Created by Sergei on 12.02.2015.
 */
public class PendulumModel extends ODEBaseModel {

    public PendulumModel() {
        setAdditionalParameter(0, 3);
        setStartParameter(0, 5);
    }

    private final TimeVector startVector = new TimeVector(size());
    private final Vector additionalVector = new Vector(additionalSize());

    @Override
    public double getODETime() {
        return AppSettings.getODETime();
    }

    @Override
    public int getNumberOfIterations() {
        return (int) (getODETime()/0.01);
    }

    @Override
    public ODEMethod getMethod() {
        return AppSettings.getODEMethod();
    }

    @Override
    public Function getFunction() {
        double om = getAdditionalParameter(0);
        return new PendulumFunction(om);
    }

    @Override
    public int size() {
        return names().length;
    }

    @Override
    public int additionalSize() {
        return additionalNames().length;
    }

    @Override
    public String[] names() {
        return new String[] {"y", "v"};
    }

    @Override
    public String[] additionalNames() {
        return new String[] {"om"};
    }

    @Override
    public TimeVector getStartVector() {
        return startVector.clone();
    }

    @Override
    public double getStartParameter(int index) {
        return startVector.get(index);
    }

    @Override
    public void setStartParameter(int index, double v) {
        if (startVector.get(index) == v) return;
        startVector.set(index, v);
        onPropertyChanged("StartParameter", null, v);
    }

    @Override
    public double getAdditionalParameter(int index) {
        return additionalVector.get(index);
    }

    @Override
    public void setAdditionalParameter(int index, double v) {
        if (additionalVector.get(index) == v) return;
        additionalVector.set(index, v);
        onPropertyChanged("AdditionalParameter", null, v);
    }
}
