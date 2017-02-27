package com.sbutterfly.engine;

import com.sbutterfly.engine.trace.Trace;
import com.sbutterfly.engine.trace.TraceDescription;

/**
 * Базовый класс, который содержит логику для обработки и хранения дифференциальных данных модели.
 *
 * @author s-ermakov
 */
public abstract class Model {
    public abstract Trace getTrace(TraceDescription traceDescription);
}
