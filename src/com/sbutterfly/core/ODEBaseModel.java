package com.sbutterfly.core;

import com.sbutterfly.differential.*;
import info.monitorenter.gui.chart.ITrace2D;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by Sergei on 12.02.2015.
 */
public abstract class ODEBaseModel implements ODEModelSerializer.ODESerializable {

    private final ArrayList<PropertyChangeListener> listeners = new ArrayList<>();
    private TimeVector[] vectors;

    public abstract double getODETime();

    public abstract int getNumberOfIterations();

    public abstract ODEMethod getMethod();

    public abstract Function getFunction();

    public abstract int size();

    public abstract int additionalSize();

    public abstract String[] names();

    public abstract String[] additionalNames();

    public abstract TimeVector getStartVector();

    public abstract double getStartParameter(int index);

    public abstract void setStartParameter(int index, double v);

    public abstract double getAdditionalParameter(int index);

    public abstract void setAdditionalParameter(int index, double v);

    public boolean hasValues() {
        return vectors != null;
    }

    public synchronized TimeVector[] values(){
        return values(true);
    }

    public synchronized TimeVector[] values(boolean useCache){
        if (vectors == null || !useCache) {
            Differential differential = new Differential(getFunction(), getStartVector(), getODETime(), getNumberOfIterations(), getMethod());
            vectors = differential.makeDifferential();
        }
        return vectors;
    }

    public void setToTrace(int yIndex, int xIndex, ITrace2D trace2D){
        TimeVector[] vectors = values();
        for (int i = 0, n = vectors.length; i < n; i++){

            double x = vectors[i].get(xIndex);
            double y = vectors[i].get(yIndex);

            trace2D.addPoint(x, y);
        }
    }

    public void setToTrace(Index yIndex, Index xIndex, ITrace2D trace2D){
        TimeVector[] vectors = values();
        for (int i = 0, n = vectors.length; i < n; i++){

            int xx = Index.toInt(xIndex);
            int yy = Index.toInt(yIndex);

            double x = xx >= 0 ? vectors[i].get(xx) : vectors[i].getTime();
            double y = yy >= 0 ? vectors[i].get(yy) : vectors[i].getTime();

            trace2D.addPoint(x, y);
        }
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

    /*
        Class used only for serialization
     */
    private class SerModel {
        TimeVector[] values;
    }
}