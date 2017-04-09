package com.sbutterfly.gui;

import com.sbutterfly.concurrency.CallbackFuture;
import com.sbutterfly.differential.Vector;
import com.sbutterfly.engine.Model;
import com.sbutterfly.engine.ModelResult;
import com.sbutterfly.engine.trace.Trace;
import com.sbutterfly.engine.trace.TraceDescription;
import com.sbutterfly.gui.helpers.EventHandler;
import com.sbutterfly.gui.helpers.EventListener;
import com.sbutterfly.gui.panels.Constraint;
import com.sbutterfly.gui.panels.JGridBagPanel;
import com.sbutterfly.utils.DoubleUtils;
import com.sbutterfly.utils.Log;
import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.IAxis;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.traces.Trace2DSimple;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author s-ermakov
 */
@SuppressWarnings("magicnumber")
public class ChartView extends JGridBagPanel {

    private final EventHandler<AxisInformation> axisInformationEventHandler = new EventHandler<>();
    private final EventHandler<Status> statusEventHandler = new EventHandler<>();

    private final List<Model> models = new LinkedList<>();
    private final Map<Model, List<ITrace2D>> modelToTrace = new HashMap<>();
    private final Map<Model, AxisInformation> modelToInformation = new HashMap<>();
    private TraceDescription currentTraceDescription;
    private volatile int processingModels = 0;

    private final Chart2D chart2D;

    public ChartView() {
        chart2D = new Chart2D();
        chart2D.setPaintLabels(false);
        add(chart2D, Constraint.create(0, 0).fill(GridBagConstraints.BOTH).weightX(1).weightY(1));

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
        List<ITrace2D> traces = modelToTrace.get(model);
        traces.forEach(t -> t.setVisible(true));
        updateAxisInformation();
    }

    public void hideModel(Model model) {
        List<ITrace2D> traces = modelToTrace.get(model);
        traces.forEach(t -> t.setVisible(false));
        updateAxisInformation();
    }

    public void refresh() {
        chart2D.removeAllTraces();
        updateAxisInformation();
        modelToTrace.clear();
        modelToInformation.clear();
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
            decreaseProcessingModels();
        }));
        future.setOnFail(exception -> SwingUtilities.invokeLater(() -> {
            Log.error(this, exception);
            JOptionPane.showMessageDialog(null, "Произошла ошибка: " + exception.getMessage());
            decreaseProcessingModels();
        }));
    }

    private void doUntrace(Model model) {
        Log.debug(this, "do untrace " + model.getName());
        List<ITrace2D> traces = modelToTrace.get(model);
        traces.forEach(t -> {
            modelToTrace.remove(model);
            modelToInformation.remove(model);
            chart2D.removeTrace(t);
            updateAxisInformation();
        });
    }

    private void updateAxisInformation() {
        List<AxisInformation> axisInformations = modelToTrace.entrySet().stream()
                .filter(e -> e.getValue().stream().anyMatch(ITrace2D::isVisible))
                .map(e -> modelToInformation.get(e.getKey()))
                .collect(Collectors.toList());
        axisInformationEventHandler.invoke(combineAxisInformation(axisInformations));
    }

    private void doTrace(Model model, ModelResult modelResult, TraceDescription traceDescription) {
        Log.debug(this, "do trace " + model.getName());
        List<Trace> traces = modelResult.getTrace(traceDescription);

        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        Point first = null;
        Point last = null;

        for (Trace trace : traces) {
            List<Vector> values = trace.getValues();
            if (values.isEmpty()) {
                throw new IllegalArgumentException("Can't add trace with empty values");
            }

            ITrace2D viewTrace = new Trace2DSimple();
            viewTrace.setColor(model.getColor());
            chart2D.addTrace(viewTrace);

            // нет смысла печатать все точки
            // достаточно взять несколько, чтобы быстрее прогружался график
            // TODO
            // такая выборка опасная
            // так как точки могут быть неравномерными
            int width = this.getWidth();
            int height = this.getHeight();
            int points = Math.max(width, height);

            int step = Math.max(values.size() / points, 1);
            int length = values.size();

            for (int i = 0; i < length; i++) {
                Vector vector = values.get(i);

                if (i % step == 0) {
                    viewTrace.addPoint(vector.get(0), vector.get(1));
                }

                max = Math.max(max, vector.get(1));
                min = Math.min(min, vector.get(1));
            }

            Vector firstValues = values.get(0);
            Vector lastValues = values.get(length - 1);

            first = new Point(firstValues.get(0), firstValues.get(1));
            last = new Point(lastValues.get(0), lastValues.get(1));

            List<ITrace2D> trace2DS = modelToTrace.computeIfAbsent(model, key -> new ArrayList<>());
            trace2DS.add(viewTrace);
        }
        modelToInformation.put(model, new AxisInformation(max, min, first, last));
        updateAxisInformation();
    }

    private void setAxisDescription(TraceDescription traceDescription) {
        String xAxisStr = traceDescription != null ? traceDescription.getXAxisName() : "";
        String yAxisStr = traceDescription != null ? traceDescription.getYAxisName() : "";

        IAxis.AxisTitle xAxisTitle = new IAxis.AxisTitle(xAxisStr);
        IAxis.AxisTitle yAxisTitle = new IAxis.AxisTitle(yAxisStr);

        xAxisTitle.setTitleFont(new Font(null, Font.PLAIN, 15));
        yAxisTitle.setTitleFont(new Font(null, Font.PLAIN, 15));

        chart2D.getAxisX().setAxisTitle(xAxisTitle);
        chart2D.getAxisY().setAxisTitle(yAxisTitle);
    }

    public void addStatusListener(EventListener<Status> eventListener) {
        statusEventHandler.add(eventListener);
    }

    public void removeStatesHandler(EventListener<Status> eventListener) {
        statusEventHandler.remove(eventListener);
    }

    public void addAxisInformationListener(EventListener<AxisInformation> axisInformationEventListener) {
        axisInformationEventHandler.add(axisInformationEventListener);
    }

    public void removeAxisInformationListener(EventListener<AxisInformation> axisInformationEventListener) {
        axisInformationEventHandler.remove(axisInformationEventListener);
    }

    private synchronized void increaseProcessingModels() {
        processingModels++;
        if (processingModels == 1) {
            statusEventHandler.invoke(Status.BUSY);
        }
    }

    private synchronized void decreaseProcessingModels() {
        processingModels--;
        assert processingModels >= 0;
        if (processingModels == 0) {
            statusEventHandler.invoke(Status.IDLE);
        }
    }

    private AxisInformation combineAxisInformation(Collection<AxisInformation> axisInformations) {
        AxisInformation result = null;
        for (AxisInformation axisInformation : axisInformations) {
            result = result == null ? axisInformation : AxisInformation.merge(axisInformation, result);
        }
        return result;
    }

    public enum Status {
        IDLE,
        BUSY
    }

    public static class AxisInformation {
        private final Double max;
        private final Double min;

        private final Point first;
        private final Point last;

        AxisInformation(Double max, Double min, Point first, Point last) {
            this.max = max;
            this.min = min;
            this.first = first;
            this.last = last;
        }

        public Double getMax() {
            return max;
        }

        public Double getMin() {
            return min;
        }

        public Point getFirst() {
            return first;
        }

        public Point getLast() {
            return last;
        }

        static AxisInformation merge(AxisInformation axisInformation1, AxisInformation axisInformation2) {
            return new AxisInformation(Math.max(axisInformation1.max, axisInformation2.max),
                    Math.min(axisInformation1.min, axisInformation2.min),
                    null, null);
        }
    }

    public static class Point {
        private double x;
        private double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        @Override
        public String toString() {
            return "x: " + DoubleUtils.toString(x) + ", y: " + DoubleUtils.toString(y);
        }
    }
}
