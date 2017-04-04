package com.sbutterfly.engine;

import com.sbutterfly.differential.TimeVector;
import com.sbutterfly.differential.Vector;
import com.sbutterfly.engine.trace.Axis;
import com.sbutterfly.engine.trace.Trace;
import com.sbutterfly.engine.trace.TraceDescription;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
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

    public List<Trace> getTrace(TraceDescription traceDescription) {
        if (this.values == null) {
            throw new IllegalStateException("values don't set");
        }

        Collection<Pair<Axis, Axis>> pairs = traceDescription.getAxises();

        List<Trace> traces = new ArrayList<>(pairs.size());

        for (Pair<Axis, Axis> axis : pairs) {
            Axis xAxis = axis.getKey();
            Axis yAxis = axis.getValue();
            List<Vector> result = new ArrayList<>(values.size());

            for (TimeVector vector : values) {
                double xValue = getValue(vector, xAxis);
                double yValue = getValue(vector, yAxis);
                result.add(new Vector(xValue, yValue));
            }
            traces.add(new Trace(traceDescription, result));
        }

        return traces;
    }

    public abstract double getValue(TimeVector vector, Axis axis);
}
