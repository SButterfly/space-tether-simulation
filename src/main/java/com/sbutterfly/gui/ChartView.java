package com.sbutterfly.gui;

import com.sbutterfly.engine.Model;
import com.sbutterfly.engine.trace.TraceDescription;
import com.sbutterfly.gui.helpers.EventHandler;
import com.sbutterfly.gui.helpers.EventListener;
import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.IAxis;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.traces.Trace2DSimple;

import java.awt.Font;
import java.util.LinkedList;
import java.util.List;

/**
 * @author s-ermakov
 */
public class ChartView extends Chart2D {

    private final EventHandler<Event> eventHandler = new EventHandler<>();
    private final List<Model> models = new LinkedList<>();

    public void addModel(Model model) {
        models.add(model);
    }

    public void removeModel(Model model) {
        models.remove(model);
    }

    public void addEventListener(EventListener<Event> listener) {
        eventHandler.add(listener);
    }

    public void removeEventListener(EventListener<Event> listener) {
        eventHandler.remove(listener);
    }

    public void refresh() {

    }

    public void showTraceDescription(TraceDescription traceDescription) {

        ITrace2D trace = new Trace2DSimple(traceable.name);
        chart.addTrace(trace);
        currentModel.setToTrace(traceable.yIndex, traceable.xIndex, trace);
        modelsListView.add(trace);

        IAxis.AxisTitle xAxisTitle = new IAxis.AxisTitle(currentModel.getFullAxisName(traceable.xIndex));
        IAxis.AxisTitle yAxisTitle = new IAxis.AxisTitle(currentModel.getFullAxisName(traceable.yIndex));

        xAxisTitle.setTitleFont(new Font(null, Font.PLAIN, 15));
        yAxisTitle.setTitleFont(new Font(null, Font.PLAIN, 15));

        chart.getAxisX().setAxisTitle(xAxisTitle);
        chart.getAxisY().setAxisTitle(yAxisTitle);
    }

    public enum Status {
        IDLE,
        IN_PROGRESS
    }

    public static class Event {
        private final Status status;
        private final double progressPercent;

        private Event(Status status) {
            this(status, 0);
        }

        private Event(Status status, double progressPercent) {
            this.status = status;
            this.progressPercent = progressPercent;
        }

        public Status getStatus() {
            return status;
        }

        public double getProgressPercent() {
            return progressPercent;
        }
    }
}
