package com.sbutterfly.engine.trace;

/**
 * Класс, описывающий необходимую зависимость координат.
 *
 * @author s-ermakov
 */
public class TraceDescription {
    private final Axis xAxis;
    private final Axis yAxis;
    private final String name;

    public TraceDescription(Axis xAxis, Axis yAxis) {
        this(xAxis, yAxis, null);
    }

    public TraceDescription(Axis xAxis, Axis yAxis, String name) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.name = name;
    }

    public Axis getXAxis() {
        return xAxis;
    }

    public Axis getYAxis() {
        return yAxis;
    }

    public String getName() {
        return name == null || name.isEmpty()
                ? getYAxis().getHumanReadableName() + " / " + getXAxis().getHumanReadableName()
                : name;
    }

    @Override
    public String toString() {
        return "TraceDescription{" +
                "name='" + getName() + '\'' +
                '}';
    }
}
