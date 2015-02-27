package com.sbutterfly.core.rope;

import com.sbutterfly.core.ODEBaseModel;
import com.sbutterfly.differential.Function;
import com.sbutterfly.differential.ODEMethod;
import com.sbutterfly.differential.TimeVector;
import com.sbutterfly.differential.Vector;
import com.sbutterfly.services.AppSettings;

import java.util.StringTokenizer;

/**
 * Created by Sergei on 27.02.2015.
 */
public class RopeModel extends ODEBaseModel {

    private static final String separateValue = "#@#";
    private final TimeVector startVector = new TimeVector(size());
    private final Vector additionalVector = new Vector(additionalSize());

    public RopeModel() {
        setStartParameter(0, 1);
        setStartParameter(1, 2.5);

        setAdditionalParameter(0, 6000);
        setAdditionalParameter(1, 20);
        setAdditionalParameter(2, 0.0002);
        setAdditionalParameter(3, 4);
        setAdditionalParameter(4, 5);
        setAdditionalParameter(5, 30000);
    }

    @Override
    public double getODETime() {
        return 20000;
    }

    @Override
    public int getNumberOfIterations() {
        return 40000;
    }

    @Override
    public ODEMethod getMethod() {
        return AppSettings.getODEMethod();
    }

    @Override
    public Function getFunction() {
        final double m1 = getAdditionalParameter(0);
        final double m2 = getAdditionalParameter(1);
        final double p = getAdditionalParameter(2);
        final double a = getAdditionalParameter(3);
        final double b = getAdditionalParameter(4);
        final double lk = getAdditionalParameter(5);

        return new RopeFunction(m1, m2, p, a, b, lk);
    }

    @Override
    public int size() {
        return 6;
    }

    @Override
    public int additionalSize() {
        return 6;
    }

    @Override
    public String[] names() {
        return new String[]{"L", "Lt", "O", "Ot", "B", "Bt"};
    }

    @Override
    public String[] additionalNames() {
        return new String[]{"m1", "m2", "p", "a", "b", "Lk"};
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
