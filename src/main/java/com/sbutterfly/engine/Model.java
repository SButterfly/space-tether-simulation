package com.sbutterfly.engine;

import com.sbutterfly.engine.trace.Axis;
import com.sbutterfly.engine.trace.Trace;
import com.sbutterfly.engine.trace.TraceDescription;
import com.sbutterfly.gui.AdditionalLineView;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
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

    /**
     * Пересчитывает значения системы
     */
    public void refresh() {


        // TODO move to Model realization

        this.currentModel = model;
        addTraceView.setEnabled(false);

        // TODO change to thread pool
        new Thread(() -> {
            try {
                model.values(false);
                AdditionalLineView.Processable p = model.getProcessable();
                if (p != null && !p.hasCanceled()) {
                    SwingUtilities.invokeLater(() -> addTraceView.init(model));
                    additionalLineView.setText("Расчет закончен");
                } else {
                    additionalLineView.setText("Расчет отменен");
                }
            } catch (Exception e) {
                AdditionalLineView.Processable p = model.getProcessable();
                if (p != null) {
                    p.cancel();
                }
                SwingUtilities.invokeLater(() -> {
                    additionalLineView.setText("Произошла ошибка");
                    JOptionPane.showMessageDialog(null, e.getMessage());
                });
            }
        }).start();

        AdditionalLineView.Processable processable = null;
        while (processable == null || processable.hasEnded()) {
            processable = model.getProcessable();
        }
        additionalLineView.setText("Выполняется расчет");
        additionalLineView.setProcessable(processable);

    }

    public enum Status {
        EMPTY, // значения отсутствуют
        IN_PROGRESS, // производится отсчет
        READY // расчет завершен
    }
}
