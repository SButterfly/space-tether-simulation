package com.sbutterfly.core;

import com.sbutterfly.GUI.AdditionalLineView;
import com.sbutterfly.differential.*;
import com.sbutterfly.services.AppSettings;
import info.monitorenter.gui.chart.ITrace2D;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by Sergei on 12.02.2015.
 */
public abstract class ODEBaseModel implements ODEModelSerializer.ODESerializable {

    private final double[] startParamsVector = new double[initialParamsNames().length];
    private final double[] initialParamsVector = new double[paramsNames().length];
    private final ArrayList<PropertyChangeListener> listeners = new ArrayList<>();
    private TimeVector[] vectors;
    private volatile Differential.DifferentialIterator iterator;

    public abstract Function getFunction();

    public double getODETime() {
        return AppSettings.getODETime();
    }

    public int getNumberOfIterations() {
        return (int) (getODETime() / AppSettings.getODEStep());
    }

    public ODEMethod getMethod() {
        return AppSettings.getODEMethod();
    }

    public String[] initialParamsNames() {
        return new String[0];
    }

    public String[] paramsNames() {
        return new String[0];
    }

    public String[] paramsExtNames() {
        return new String[0];
    }

    public String[] customParamsNames() {
        return new String[]{"t"};
    }

    public String[] customParamsExtNames() {
        return new String[]{"с"};
    }

    public TimeVector getStartParamsVector() {
        return new TimeVector(0, startParamsVector);
    }

    public double getStartParameter(int index) {
        return startParamsVector[index];
    }

    public void setStartParameter(int index, double v) {
        if (startParamsVector[index] == v) return;
        startParamsVector[index] = v;
        onPropertyChanged("StartParameter", null, v);
    }

    public double getInitialParameter(int index) {
        return initialParamsVector[index];
    }

    public void setInitialParameter(int index, double v) {
        if (initialParamsVector[index] == v) return;
        initialParamsVector[index] = v;
        onPropertyChanged("AdditionalParameter", null, v);
    }

    public Customable getCustomable(int index) {
        if (index != 0) throw new NumberFormatException("Index is out of diapason");
        return new Customable() {
            @Override
            public double customize(TimeVector vector) {
                return vector.getTime();
            }
        };
    }

    public synchronized TimeVector[] values(){
        return values(true);
    }

    public synchronized TimeVector[] values(boolean useCache){
        if (vectors == null || !useCache) {
            Differential differential = new Differential(getFunction(), getStartParamsVector(), getODETime(), getNumberOfIterations(), getMethod());
            iterator = differential.iterator();
            vectors = differential.makeDifferential(iterator);
            if (iterator.hasCanceled()) {
                vectors = null;
            }
        }
        return vectors;
    }

    public AdditionalLineView.Processable getProcessable() {
        return iterator;
    }

    public void setToTrace(Index yIndex, Index xIndex, ITrace2D trace2D) {
        TimeVector[] vectors = values();
        for (int i = 0, n = vectors.length; i < n; i++){

            final double x = getValue(vectors[i], xIndex);
            final double y = getValue(vectors[i], yIndex);

            trace2D.addPoint(x, y);
        }
    }

    public double getValue(TimeVector vector, Index index) {
        Customable customable = index.getCustomable();
        return customable == null ? vector.get(index.getIndex()) : customable.customize(vector);
    }

    public String getName(Index index) {
        Customable customable = index.getCustomable();
        return customable == null ? paramsNames()[index.getIndex()] : customParamsNames()[index.getIndex()];
    }

    public String getExtName(Index index) {
        Customable customable = index.getCustomable();
        return customable == null ? paramsExtNames()[index.getIndex()] : customParamsExtNames()[index.getIndex()];
    }

    public String getFullAxisName(Index index) {
        return getName(index) + ", " + getExtName(index);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener){
        listeners.add(listener);
    }
    public void removePropertyChangeListener(PropertyChangeListener listener){
        listeners.remove(listener);
    }
    public synchronized void onPropertyChanged(String propertyName, Object oldValue, Object newValue){
        vectors = null;
        for (PropertyChangeListener listener : listeners){
            listener.propertyChange(new PropertyChangeEvent(this, propertyName, oldValue, newValue));
        }
    }

    public void dispose(){
        vectors = null;
    }

    @Override
    public String serialize() {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(startParamsVector.length);
        stringBuilder.append(' ');
        for (int i = 0, n = startParamsVector.length; i < n; i++) {
            stringBuilder.append(getStartParameter(i));
            stringBuilder.append(' ');
        }

        stringBuilder.append(initialParamsVector.length);
        stringBuilder.append(' ');
        for (int i = 0, n = initialParamsVector.length; i < n; i++) {
            stringBuilder.append(getInitialParameter(i));
            stringBuilder.append(' ');
        }

        TimeVector[] values = values();

        stringBuilder.append(values.length);
        stringBuilder.append(' ');

        for (TimeVector vector : values) {
            stringBuilder.append(vector.getTime());
            stringBuilder.append(' ');

            stringBuilder.append(vector.size());
            stringBuilder.append(' ');

            for (Double x : vector) {
                stringBuilder.append(x);
                stringBuilder.append(' ');
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public void deserialize(String value) {
        StringTokenizer tokenizer = new StringTokenizer(value);

        int startParamsSize = Integer.parseInt(tokenizer.nextToken());
        for (int i = 0; i < startParamsSize; i++) {
            double val = Double.parseDouble(tokenizer.nextToken());
            setStartParameter(i, val);
        }

        int intialParamsSize = Integer.parseInt(tokenizer.nextToken());
        for (int j = 0; j < intialParamsSize; j++) {
            double val = Double.parseDouble(tokenizer.nextToken());
            setInitialParameter(j, val);
        }

        int size = Integer.parseInt(tokenizer.nextToken());
        TimeVector[] values = new TimeVector[size];

        for (int i = 0; i < size; i++) {
            double time = Double.parseDouble(tokenizer.nextToken());
            int length = Integer.parseInt(tokenizer.nextToken());

            double[] vector = new double[length];
            for (int j = 0; j < length; j++) {
                vector[j] = Double.parseDouble(tokenizer.nextToken());
            }
            values[i] = new TimeVector(time, vector);
        }

        this.vectors = values;
    }
}