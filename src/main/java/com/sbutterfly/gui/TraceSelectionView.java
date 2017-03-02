package com.sbutterfly.gui;

import com.sbutterfly.engine.trace.TraceDescription;
import com.sbutterfly.gui.helpers.EventHandler;
import com.sbutterfly.gui.helpers.EventListener;
import com.sbutterfly.gui.panels.JGridBagPanel;

import java.util.List;

/**
 * @author s-ermakov
 */
public class TraceSelectionView extends JGridBagPanel {

    private final EventHandler<Event> eventHandler = new EventHandler<>();

    private List<TraceDescription> traceDescriptions;

    public void setTraceDescriptions(List<TraceDescription> traceDescriptions) {
        this.traceDescriptions = traceDescriptions;
    }

    public List<TraceDescription> getTraceDescriptions() {
        return traceDescriptions;
    }

    public void addEventListener(EventListener<Event> listener) {
        eventHandler.add(listener);
    }

    public void removeEventListener(EventListener<Event> listener) {
        eventHandler.remove(listener);
    }

    public class Event {
        private final TraceDescription traceDescription;

        public Event(TraceDescription traceDescription) {
            this.traceDescription = traceDescription;
        }

        public TraceDescription getTraceDescription() {
            return traceDescription;
        }
    }
}
