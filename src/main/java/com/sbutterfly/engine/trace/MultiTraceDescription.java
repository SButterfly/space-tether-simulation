package com.sbutterfly.engine.trace;

import javafx.util.Pair;

import java.util.Arrays;
import java.util.Collection;

/**
 * Класс, описывающий необходимую зависимость координат.
 *
 * @author s-ermakov
 */
public class MultiTraceDescription implements TraceDescription {

    private final String xAxis;
    private final String yAxis;
    private final Collection<Pair<Axis, Axis>> collection;

    public MultiTraceDescription(String xAxisName, String yAxisName, Pair<Axis, Axis>... values) {
        this(xAxisName, yAxisName, Arrays.asList(values));
    }

    public MultiTraceDescription(String xAxisName, String yAxisName, Collection<Pair<Axis, Axis>> collection) {
        this.xAxis = xAxisName;
        this.yAxis = yAxisName;
        this.collection = collection;
    }

    @Override
    public Collection<Pair<Axis, Axis>> getAxises() {
        return collection;
    }

    @Override
    public String getXAxisName() {
        return xAxis;
    }

    @Override
    public String getYAxisName() {
        return yAxis;
    }

    @Override
    public String toString() {
        return "TraceDescription{" +
                "name='" + getName() + '\'' +
                '}';
    }
}
