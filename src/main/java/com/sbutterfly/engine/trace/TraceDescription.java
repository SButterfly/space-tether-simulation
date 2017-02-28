package com.sbutterfly.engine.trace;

/**
 * Класс, описывающий необходимую зависимость координат.
 *
 * @author s-ermakov
 */
public class TraceDescription {
    private final Axis xAxis;
    private final Axis yAxis;

    private String name;

    public TraceDescription(Axis xAxis, Axis yAxis) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }

    public Axis getxAxis() {
        return xAxis;
    }

    public Axis getyAxis() {
        return yAxis;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TraceDescription{" +
                "xAxis=" + xAxis +
                ", yAxis=" + yAxis +
                ", name='" + name + '\'' +
                '}';
    }
}
