package com.sbutterfly.engine;

import com.sbutterfly.engine.trace.TraceDescription;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, модержащий набор моделей одного типа.
 *
 * @author s-ermakov
 */
public abstract class ModelSet extends ArrayList<Model> {

    /**
     * Создает новую модель, но не сохраняет ее.
     */
    public abstract Model createModel();

    public abstract List<TraceDescription> getModelTraces();
}
