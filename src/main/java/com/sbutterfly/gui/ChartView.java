package com.sbutterfly.gui;

import com.sbutterfly.engine.Model;
import com.sbutterfly.engine.trace.Trace;
import com.sbutterfly.engine.trace.TraceDescription;
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
@SuppressWarnings("magicnumber")
public class ChartView extends Chart2D {

    private final List<Model> models = new LinkedList<>();
    private TraceDescription currentTraceDescription;

    public ChartView() {
        setPaintLabels(false);
        refresh();
    }

    public void addModel(Model model) {
        models.add(model);
    }

    public void removeModel(Model model) {
        models.remove(model);
    }

    public void refresh() {
        super.removeAll();
        setAxisDescription(currentTraceDescription);
        if (currentTraceDescription != null) {
            models.forEach(m -> addTrace(m, currentTraceDescription));
        }
    }

    public void showTraceDescription(TraceDescription traceDescription) {
        this.currentTraceDescription = traceDescription;
        refresh();
    }

    private void addTrace(Model model, TraceDescription traceDescription) {
        Trace trace = model.getTrace(traceDescription);
        ITrace2D viewTrace = new Trace2DSimple();
        trace.getValues().forEach(v -> viewTrace.addPoint(v.get(0), v.get(1)));
        this.addTrace(viewTrace);
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
    }
}
