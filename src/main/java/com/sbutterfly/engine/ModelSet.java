package com.sbutterfly.engine;

import com.sbutterfly.engine.trace.TraceDescription;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, модержащий набор моделей одного типа.
 *
 * @author s-ermakov
 */
public abstract class ModelSet<T extends Model> extends ArrayList<T> {

    public abstract List<TraceDescription> getModelTraces();
}
