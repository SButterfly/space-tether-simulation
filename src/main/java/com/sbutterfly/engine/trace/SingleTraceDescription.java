package com.sbutterfly.engine.trace;

import javafx.util.Pair;

import java.util.Collection;
import java.util.Collections;

/**
 * Класс, описывающий необходимую зависимость координат.
 *
 * @author s-ermakov
 */
public class SingleTraceDescription implements TraceDescription {
    private final Axis xAxis;
    private final Axis yAxis;
    private final String name;

    public SingleTraceDescription(Axis xAxis, Axis yAxis) {
        this(xAxis, yAxis, null);
    }

    public SingleTraceDescription(Axis xAxis, Axis yAxis, String name) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.name = name;
    }

    @Override
    public Collection<Pair<Axis, Axis>> getAxises() {
        return Collections.singleton(new Pair<>(xAxis, yAxis));
    }

    @Override
    public String getXAxisName() {
        return xAxis.getHumanReadableName();
    }

    @Override
    public String getYAxisName() {
        return yAxis.getHumanReadableName();
    }

    public String getName() {
        return name == null || name.isEmpty()
                ? getYAxisName() + " / " + getXAxisName()
                : name;
    }

    @Override
    public String toString() {
        return "TraceDescription{" +
                "name='" + getName() + '\'' +
                '}';
    }
}
