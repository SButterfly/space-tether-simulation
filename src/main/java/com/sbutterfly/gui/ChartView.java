package com.sbutterfly.gui;

import com.sbutterfly.concurrency.CallbackFuture;
import com.sbutterfly.differential.Vector;
import com.sbutterfly.engine.Model;
import com.sbutterfly.engine.ModelResult;
import com.sbutterfly.engine.trace.Trace;
import com.sbutterfly.engine.trace.TraceDescription;
import com.sbutterfly.gui.helpers.EventHandler;
import com.sbutterfly.gui.helpers.EventListener;
import com.sbutterfly.utils.Log;
import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.IAxis;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.traces.Trace2DSimple;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.Font;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author s-ermakov
 */
@SuppressWarnings("magicnumber")
public class ChartView extends Chart2D {

    private final EventHandler<Status> statusEventHandler = new EventHandler<>();

    private final List<Model> models = new LinkedList<>();
    private final Map<Model, ITrace2D> modelToTrace = new HashMap<>();
    private TraceDescription currentTraceDescription;

    private int processingModels = 0;

    public ChartView() {
        setPaintLabels(false);
        refresh();
    }

    public void addModel(Model model) {
        models.add(model);
        if (currentTraceDescription != null) {
            addTrace(model, currentTraceDescription);
        }
    }

    public void removeModel(Model model) {
        models.remove(model);
        removeTrace(model);
    }

    public void appearModel(Model model) {
        ITrace2D trace2D = modelToTrace.get(model);
        trace2D.setVisible(true);
    }

    public void hideModel(Model model) {
        ITrace2D trace2D = modelToTrace.get(model);
        trace2D.setVisible(false);
    }

    public void refresh() {
        super.removeAllTraces();
        modelToTrace.clear();
        setAxisDescription(currentTraceDescription);
        if (currentTraceDescription != null) {
            models.forEach(m -> addTrace(m, currentTraceDescription));
        }
    }

    public void showTraceDescription(TraceDescription traceDescription) {
        this.currentTraceDescription = traceDescription;
        refresh();
    }

    private void removeTrace(Model model) {
        Log.debug(this, "remove trace " + model.getName());
        CallbackFuture<? extends ModelResult> future = model.getValuesFuture();

        // set null to avoid painting
        future.setOnSuccess(null);
        future.setOnFail(null);

        doUntrace(model);
    }

    private void addTrace(Model model, TraceDescription traceDescription) {
        Log.debug(this, "Add trace " + model.getName());
        CallbackFuture<? extends ModelResult> future = model.getValuesFuture();
        increaseProcessingModels();
        future.setOnSuccess(modelResult -> SwingUtilities.invokeLater(() -> {
            doTrace(model, modelResult, traceDescription);
        }));
        future.setOnFail(exception -> SwingUtilities.invokeLater(() -> {
            decreaseProcessingModels();
            Log.error(this, exception);
            JOptionPane.showMessageDialog(null, "Произошла ошибка: " + exception.getMessage());
        }));
    }

    private void doUntrace(Model model) {
        Log.debug(this, "do untrace " + model.getName());
        ITrace2D trace2D = modelToTrace.get(model);
        if (trace2D != null) {
            this.removeTrace(trace2D);

            modelToTrace.remove(model);
            decreaseProcessingModels();
        }
    }

    private void doTrace(Model model, ModelResult modelResult, TraceDescription traceDescription) {
        Log.debug(this, "do trace " + model.getName());
        Trace trace = modelResult.getTrace(traceDescription);
        ITrace2D viewTrace = new Trace2DSimple();
        viewTrace.setColor(model.getColor());
        this.addTrace(viewTrace);

        // нет смысла печатать все точки
        // достаточно взять несколько, чтобы быстрее прогружался график

        // TODO
        // такая выборка опасная
        // так как точки могут быть неравномерными
        int width = this.getWidth();
        int height = this.getHeight();
        int points = 5 * Math.max(width, height); // с коэффициентом 5 лучше линии

        List<Vector> values = trace.getValues();

        int step = Math.max(values.size() / points, 1);
        for (int i = 0; i < values.size(); i += step) {
            viewTrace.addPoint(values.get(i).get(0), values.get(i).get(1));
        }
        modelToTrace.put(model, viewTrace);
        decreaseProcessingModels();
    }

    private void setAxisDescription(TraceDescription traceDescription) {
        String xAxisStr = traceDescription != null ? traceDescription.getXAxis().getHumanReadableName() : "";
        String yAxisStr = traceDescription != null ? traceDescription.getYAxis().getHumanReadableName() : "";

        IAxis.AxisTitle xAxisTitle = new IAxis.AxisTitle(xAxisStr);
        IAxis.AxisTitle yAxisTitle = new IAxis.AxisTitle(yAxisStr);

        xAxisTitle.setTitleFont(new Font(null, Font.PLAIN, 15));
        yAxisTitle.setTitleFont(new Font(null, Font.PLAIN, 15));

        this.getAxisX().setAxisTitle(xAxisTitle);
        this.getAxisY().setAxisTitle(yAxisTitle);
        this.updateUI();
    }

    public void addStatusListener(EventListener<Status> eventListener) {
        statusEventHandler.add(eventListener);
    }

    public void removeStatesHandler(EventListener<Status> eventListener) {
        statusEventHandler.remove(eventListener);
    }

    private void increaseProcessingModels() {
        processingModels++;
        if (processingModels == 1) {
            statusEventHandler.invoke(Status.BUSY);
        }
    }

    private void decreaseProcessingModels() {
        processingModels--;
        assert processingModels >= 0;
        if (processingModels == 0) {
            statusEventHandler.invoke(Status.IDLE);
        }
    }

    public enum Status {
        IDLE,
        BUSY
    }
}
