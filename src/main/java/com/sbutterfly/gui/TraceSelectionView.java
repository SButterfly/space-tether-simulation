package com.sbutterfly.gui;

import com.sbutterfly.engine.trace.TraceDescription;
import com.sbutterfly.gui.helpers.EventHandler;
import com.sbutterfly.gui.helpers.EventListener;
import com.sbutterfly.gui.panels.Constraint;
import com.sbutterfly.gui.panels.JGridBagPanel;
import com.sbutterfly.utils.Log;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author s-ermakov
 */
public class TraceSelectionView extends JGridBagPanel {

    private final EventHandler<Event> eventHandler = new EventHandler<>();

    private Map<String, TraceDescription> traceDescriptionMap = new HashMap<>();

    private JComboBox<String> comboBox;

    public TraceSelectionView() {
        createGUI();
    }

    private void createGUI() {
        JLabel namePanel = new JLabel("График:");
        comboBox = new JComboBox<>();

        add(namePanel, Constraint.create(0, 0).weightX(1).fill(GridBagConstraints.HORIZONTAL).anchor(GridBagConstraints.WEST));
        add(comboBox, Constraint.create(0, 1, 1, 1).weightX(1).fill(GridBagConstraints.HORIZONTAL).anchor(GridBagConstraints.WEST));

        comboBox.addActionListener(a -> {
            TraceDescription traceDescription = traceDescriptionMap.get(comboBox.getSelectedItem());
            eventHandler.invoke(new Event(traceDescription));
        });

        Log.debug(this, "GUI was created");
    }

    public void setTraceDescriptions(List<TraceDescription> traceDescriptions) {
        comboBox.removeAllItems();
        traceDescriptions.forEach(td -> {
            traceDescriptionMap.put(td.getName(), td);
            comboBox.addItem(td.getName());
        });
    }

    public List<TraceDescription> getTraceDescriptions() {
        return new ArrayList<>(traceDescriptionMap.values());
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
