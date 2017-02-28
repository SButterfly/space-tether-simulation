package com.sbutterfly.engine;

import com.sbutterfly.engine.trace.Axis;
import com.sbutterfly.engine.trace.Trace;
import com.sbutterfly.engine.trace.TraceDescription;

import java.awt.Color;
import java.util.List;

/**
 * Базовый класс, который содержит логику для обработки и хранения дифференциальных данных модели.
 *
 * @author s-ermakov
 */
public abstract class Model {

    private String name;
    private Color color;

    public String getName() {
        return name == null ? "Безымянный" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Возвращает описание модели в ввиде списка групп параметров, необходимых к заданию.
     */
    public abstract List<GroupAxisDescription> getModelDescription();

    /**
     * Возвращает значение определенное по-умолчанию.
     */
    public abstract Double getInitialValue(Axis axis);

    /**
     * Задает значение по умолчанию.
     */
    public abstract void setInitialValue(Axis k, Double v);

    public abstract Trace getTrace(TraceDescription traceDescription);

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
