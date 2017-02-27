package com.sbutterfly.engine.trace;

/**
 * Класс, описывающий необходимую зависимость координат.
 *
 * @author s-ermakov
 */
public class TraceDescription {
    private final Axis xAxis;
    private final Axis yAxis;

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

    @Override
    public String toString() {
        return "Trace{" +
                "xAxis=" + xAxis +
                ", yAxis='" + yAxis + '\'' +
                '}';
    }
}
