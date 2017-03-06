package com.sbutterfly.engine;

import com.sbutterfly.differential.Differential;
import com.sbutterfly.differential.DifferentialResult;
import com.sbutterfly.differential.Function;
import com.sbutterfly.differential.TimeVector;
import com.sbutterfly.differential.Vector;
import com.sbutterfly.engine.trace.Axis;
import com.sbutterfly.engine.trace.Trace;
import com.sbutterfly.engine.trace.TraceDescription;
import com.sbutterfly.gui.helpers.EventHandler;
import com.sbutterfly.services.AppSettings;
import com.sbutterfly.services.Execution;

import javax.swing.JOptionPane;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Базовый класс, который содержит логику для обработки и хранения дифференциальных данных модели.
 *
 * @author s-ermakov
 */
public abstract class Model {

    private final EventHandler<Event> eventHandler = new EventHandler<>();

    private String name;
    private Color color;

    private Map<Axis, Double> initialValues = new HashMap<>();
    private DifferentialResult differentialResult;

    private Status status = Status.EMPTY;

    public String getName() {
        return name == null ? "Безымянный" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Status getStatus() {
        return status;
    }

    /**
     * Возвращает значение определенное по-умолчанию.
     */
    public double getInitialValue(Axis axis) {
        return initialValues.getOrDefault(axis, 0.0);
    }

    /**
     * Задает значение по умолчанию.
     */
    public void setInitialValue(Axis axis, double value) {
        initialValues.put(axis, value);
    }

    public Trace getTrace(TraceDescription traceDescription) {
        Axis xAxis = traceDescription.getXAxis();
        Axis yAxis = traceDescription.getYAxis();

        List<TimeVector> values = getValues();
        List<Vector> result = new ArrayList<>(values.size());

        for (TimeVector vector : values) {
            double xValue = getValue(vector, xAxis);
            double yValue = getValue(vector, yAxis);
            result.add(new Vector(xValue, yValue));
        }

        return new Trace(traceDescription, result);
    }

    public List<TimeVector> getValues() {
        if (differentialResult == null) {
            Differential differential = new Differential(getFunction(), getStartTimeVector(), AppSettings.getODETime(),
                    (int) (AppSettings.getODETime() / AppSettings.getODEStep()), AppSettings.getODEMethod());

            differentialResult = differential.different();
        }
        return differentialResult.getValues();
    }

    /**
     * Возвращает описание модели в ввиде списка групп параметров, необходимых к заданию.
     */
    public abstract List<GroupAxisDescription> getModelDescription();

    protected abstract TimeVector getStartTimeVector();

    protected abstract Function getFunction();

    protected abstract double getValue(TimeVector timeVector, Axis axis);

    /**
     * Пересчитывает значения системы.
     */
    public void refresh() {
        differentialResult = null;
        setStatus(Status.EMPTY);
        try {
            setStatus(Status.IN_PROGRESS);
            getValues();
            setStatus(Status.READY);
        } catch (Exception e) {
            setStatus(Status.FAILED);

            // TODO quickfix
            Execution.submitInMain(() -> JOptionPane.showMessageDialog(null, e.getMessage()));
        }
    }

    private void setStatus(Status status) {
        this.status = status;
        eventHandler.invoke(new Event(status, this));
    }

    public enum Status {
        EMPTY, // значения отсутствуют
        IN_PROGRESS, // производится расчет
        READY, // расчет завершен
        FAILED // расчет упал
    }

    public class Event {
        private final Status status;
        private final Model model;

        private double percent;

        public Event(Status status, Model model) {
            this.status = status;
            this.model = model;
        }

        public Status getStatus() {
            return status;
        }

        public Model getModel() {
            return model;
        }

        public double getPercent() {
            return percent;
        }

        public void setPercent(double percent) {
            this.percent = percent;
        }
    }
}
