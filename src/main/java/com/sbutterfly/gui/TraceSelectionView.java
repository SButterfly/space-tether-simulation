package com.sbutterfly.gui;

import com.sbutterfly.engine.trace.TraceDescription;
import com.sbutterfly.gui.panels.JGridBagPanel;

import java.util.List;

/**
 * @author s-ermakov
 */
public class TraceSelectionView extends JGridBagPanel {
    private List<TraceDescription> traceDescriptions;

    public void setTraceDescriptions(List<TraceDescription> traceDescriptions) {
        this.traceDescriptions = traceDescriptions;
    }

    public List<TraceDescription> getTraceDescriptions() {
        return traceDescriptions;
    }
}
