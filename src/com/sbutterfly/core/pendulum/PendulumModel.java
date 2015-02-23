package com.sbutterfly.core.pendulum;

import com.sbutterfly.core.ODEBaseModel;
import com.sbutterfly.differential.Function;
import com.sbutterfly.differential.ODEMethod;
import com.sbutterfly.differential.TimeVector;
import com.sbutterfly.differential.Vector;
import com.sbutterfly.services.AppSettings;

import java.util.StringTokenizer;

/**
 * Created by Sergei on 12.02.2015.
 */
public class PendulumModel extends ODEBaseModel {

    private static final String separateValue = "#@#";
    private final TimeVector startVector = new TimeVector(size());
    private final Vector additionalVector = new Vector(additionalSize());

    public PendulumModel() {
        setAdditionalParameter(0, 3);
        setStartParameter(0, 5);
    }

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

    @Override
    public String serialize() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(size());
        stringBuilder.append(' ');
        for (int i = 0, n = size(); i < n; i++) {
            stringBuilder.append(getStartParameter(i));
            stringBuilder.append(' ');
        }

        stringBuilder.append(additionalSize());
        stringBuilder.append(' ');
        for (int i = 0, n = additionalSize(); i < n; i++) {
            stringBuilder.append(getAdditionalParameter(i));
            stringBuilder.append(' ');
        }

        return stringBuilder.toString() + separateValue + super.serialize();
    }

    @Override
    public void deserialize(String value) {
        String[] str = value.split(separateValue);

        StringTokenizer tokenizer = new StringTokenizer(str[0]);

        int size = Integer.parseInt(tokenizer.nextToken());
        for (int i = 0; i < size; i++) {
            double val = Double.parseDouble(tokenizer.nextToken());
            setStartParameter(i, val);
        }

        int additionalSize = Integer.parseInt(tokenizer.nextToken());
        for (int j = 0; j < additionalSize; j++) {
            double val = Double.parseDouble(tokenizer.nextToken());
            setAdditionalParameter(j, val);
        }

        super.deserialize(str[1]);
    }
}
