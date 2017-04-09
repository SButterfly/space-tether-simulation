package com.sbutterfly.engine.trace;

import javafx.util.Pair;

import java.util.Collection;

/**
 * @author s-ermakov
 */
public interface TraceDescription {
    Collection<Pair<Axis, Axis>> getAxises();

    String getXAxisName();
    String getYAxisName();

    String getName();
}
