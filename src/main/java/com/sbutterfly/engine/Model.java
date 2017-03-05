package com.sbutterfly.engine;

import com.sbutterfly.core.BaseSystem;
import com.sbutterfly.engine.trace.Axis;
import com.sbutterfly.engine.trace.Trace;
import com.sbutterfly.engine.trace.TraceDescription;
import com.sbutterfly.gui.helpers.EventHandler;
import com.sbutterfly.services.Execution;

import javax.swing.JOptionPane;
import java.awt.Color;
import java.util.List;

/**
 * Базовый класс, который содержит логику для обработки и хранения дифференциальных данных модели.
 *
 * @author s-ermakov
 */
public abstract class Model {

    private final EventHandler<Event> eventHandler = new EventHandler<>();

    private Status status = Status.EMPTY;

    private String name;
    private Color color;
    private BaseSystem system;

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

    public Status getStatus() {
        return status;
    }

    private void setStatus(Status status) {
        this.status = status;
        eventHandler.invoke(new Event(status, this));
    }

    /**
     * Пересчитывает значения системы.
     */
    public void refresh() {
        system.clear();
        setStatus(Status.EMPTY);
        Execution.submit(() -> {
            try {
                setStatus(Status.IN_PROGRESS);
                system.values(false);
                setStatus(Status.READY);
            } catch (Exception e) {
                system.clear();
                setStatus(Status.FAILED);

                // TODO quickfix
                Execution.submitInMain(() -> JOptionPane.showMessageDialog(null, e.getMessage()));
            }
        });
    }

    public BaseSystem getSystem() {
        return system;
    }

    public enum Status {
        EMPTY, // значения отсутствуют
        IN_PROGRESS, // производится отсчет
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
