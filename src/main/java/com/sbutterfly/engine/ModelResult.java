package com.sbutterfly.engine;

import com.sbutterfly.differential.TimeVector;
import com.sbutterfly.differential.Vector;
import com.sbutterfly.engine.trace.Axis;
import com.sbutterfly.engine.trace.Trace;
import com.sbutterfly.engine.trace.TraceDescription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс, хранящий результаты интегрирования.
 *
 * @author s-ermakov
 */
public abstract class ModelResult {

    private final Map<Axis, Double> initialValues = new HashMap<>();

    private List<TimeVector> values;

    public Map<Axis, Double> getInitialValues() {
        return initialValues;
    }

    public double getInitialValue(Axis axis) {
        return initialValues.getOrDefault(axis, 0.0);
    }

    public void setInitialValue(Axis axis, double value) {
        initialValues.put(axis, value);
    }

    public List<TimeVector> getValues() {
        return values;
    }

    public void setValues(List<TimeVector> values) {
        this.values = values;
    }

    public Trace getTrace(TraceDescription traceDescription) {
        if (this.values == null) {
            throw new IllegalStateException("values don't set");
        }

        Axis xAxis = traceDescription.getXAxis();
        Axis yAxis = traceDescription.getYAxis();

        List<Vector> result = new ArrayList<>(values.size());

        for (TimeVector vector : values) {
            double xValue = getValue(vector, xAxis);
            double yValue = getValue(vector, yAxis);
            result.add(new Vector(xValue, yValue));
        }

        return new Trace(traceDescription, result);
    }

    public abstract double getValue(TimeVector vector, Axis axis);
}
