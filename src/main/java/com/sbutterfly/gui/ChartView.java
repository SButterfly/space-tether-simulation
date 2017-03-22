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

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author s-ermakov
 */
@SuppressWarnings("magicnumber")
public class ChartView extends JGridBagPanel {

    private final EventHandler<Status> statusEventHandler = new EventHandler<>();

    private final List<Model> models = new LinkedList<>();
    private final Map<Model, ITrace2D> modelToTrace = new HashMap<>();
    private final Map<Model, AxisInformation> modelToInformation = new HashMap<>();
    private TraceDescription currentTraceDescription;
    private volatile int processingModels = 0;

    private final Chart2D chart2D;
    private final JLabel informationLabel;

    public ChartView() {
        chart2D = new Chart2D();
        chart2D.setPaintLabels(false);

        informationLabel = new JLabel();
        informationLabel.setBackground(new Color(0, 0, 0, 50));
        informationLabel.setOpaque(true);
        Border paddingBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        informationLabel.setBorder(BorderFactory.createCompoundBorder(null, paddingBorder));

        add(informationLabel, Constraint.create(0, 0).anchor(GridBagConstraints.NORTHEAST).insets(5));
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
        ITrace2D trace2D = modelToTrace.get(model);
        trace2D.setVisible(true);
    }

    public void hideModel(Model model) {
        ITrace2D trace2D = modelToTrace.get(model);
        trace2D.setVisible(false);
    }

    public void refresh() {
        chart2D.removeAllTraces();
        updateAxisInformation(null);
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
        ITrace2D trace2D = modelToTrace.get(model);
        if (trace2D != null) {
            modelToTrace.remove(model);
            modelToInformation.remove(model);
            chart2D.removeTrace(trace2D);
            updateAxisInformation(combineAxisInformation(modelToInformation.values()));
        }
    }

    private void doTrace(Model model, ModelResult modelResult, TraceDescription traceDescription) {
        Log.debug(this, "do trace " + model.getName());
        Trace trace = modelResult.getTrace(traceDescription);

        List<Vector> values = trace.getValues();
        if (values.isEmpty()) {
            throw new IllegalArgumentException("Can't add trace with empty values");
        }

        ITrace2D viewTrace = new Trace2DSimple();
        viewTrace.setColor(model.getColor());
        chart2D.addTrace(viewTrace);

        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        double first = 0;
        double last = 0;

        // нет смысла печатать все точки
        // достаточно взять несколько, чтобы быстрее прогружался график
        // TODO
        // такая выборка опасная
        // так как точки могут быть неравномерными
        int width = this.getWidth();
        int height = this.getHeight();
        int points = 3 * Math.max(width, height); // с коэффициентом 3 лучше линии

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

        first = values.get(0).get(1);
        last = values.get(length - 1).get(1);
        modelToTrace.put(model, viewTrace);
        modelToInformation.put(model, new AxisInformation(max, min, first, last));
        updateAxisInformation(combineAxisInformation(modelToInformation.values()));
    }

    private void setAxisDescription(TraceDescription traceDescription) {
        String xAxisStr = traceDescription != null ? traceDescription.getXAxis().getHumanReadableName() : "";
        String yAxisStr = traceDescription != null ? traceDescription.getYAxis().getHumanReadableName() : "";

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

    private void updateAxisInformation(AxisInformation axisInformation) {
        StringBuilder stringBuilder = new StringBuilder();
        if (axisInformation != null) {
            Double max = axisInformation.max;
            Double min = axisInformation.min;
            Double first = axisInformation.first;
            Double last = axisInformation.last;

            stringBuilder.append("<html>");
            if (max != null) {
                stringBuilder.append("Максимум: ").append(DoubleUtils.toString(max)).append("<br>");
            }
            if (min != null) {
                stringBuilder.append("Минимум: ").append(DoubleUtils.toString(min)).append("<br>");
            }
            if (first != null) {
                stringBuilder.append("Начальное: ").append(DoubleUtils.toString(first)).append("<br>");
            }
            if (last != null) {
                stringBuilder.append("Конечное: ").append(DoubleUtils.toString(last)).append("<br>");
            }
            stringBuilder.append("</html>");
        }

        SwingUtilities.invokeLater(() -> informationLabel.setText(stringBuilder.toString()));
    }

    public enum Status {
        IDLE,
        BUSY
    }

    private static class AxisInformation {
        private final Double max;
        private final Double min;
        private final Double first;
        private final Double last;

        AxisInformation(Double max, Double min, Double first, Double last) {
            this.max = max;
            this.min = min;
            this.first = first;
            this.last = last;
        }

        static AxisInformation merge(AxisInformation axisInformation1, AxisInformation axisInformation2) {
            return new AxisInformation(Math.max(axisInformation1.max, axisInformation2.max),
                    Math.min(axisInformation1.min, axisInformation2.min),
                    null, null);
        }
    }
}
