package com.sbutterfly.engine.trace;

import com.sbutterfly.differential.Vector;

import java.util.List;

/**
 * Класс, описывающий график (названия осей и значения).
 *
 * @author s-ermakov
 */
public class Trace {
    private final TraceDescription traceDescription;
    private final List<Vector> vectorList;

    public Trace(TraceDescription traceDescription, List<Vector> vectorList) {
        this.traceDescription = traceDescription;
        this.vectorList = vectorList;
    }

    public TraceDescription getTraceDescription() {
        return traceDescription;
    }

    public List<Vector> getValues() {
        return vectorList;
    }
}
