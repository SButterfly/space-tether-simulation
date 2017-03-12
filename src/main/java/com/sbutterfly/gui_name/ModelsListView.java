package com.sbutterfly.gui_name;

import com.sbutterfly.engine.Model;
import com.sbutterfly.gui_name.controls.EmptyPanel;
import com.sbutterfly.gui_name.controls.JImageButton;
import com.sbutterfly.gui_name.helpers.EventHandler;
import com.sbutterfly.gui_name.helpers.EventListener;
import com.sbutterfly.gui_name.panels.Constraint;
import com.sbutterfly.gui_name.panels.JGridBagPanel;
import com.sbutterfly.utils.Log;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Sergei on 14.02.2015.
 */
@SuppressWarnings("magicnumber")
public class ModelsListView extends JGridBagPanel {

    private static final URL DELETE_IMAGE = ModelsListView.class
            .getClassLoader().getResource("delete.png");

    private JGridBagPanel listPanel;

    private int rowCount = 0;
    private JButton deleteAllButton;
    private EventHandler<ModelEvent> eventHandler = new EventHandler<>();
    private Map<Model, Item> modelItemMap = new HashMap<>();
    private Model selectedModel;

    public ModelsListView() {
        createGUI();
        updateDeleteAllButtonState();
    }

    private void createGUI() {
        listPanel = new JGridBagPanel();

        JLabel namePanel = new JLabel("Название");
        JLabel visibilityPanel = new JLabel("Видимость");
        JLabel deletePanel = new JLabel("Удалить");

        listPanel.add(namePanel, getConstraint(0, 0));
        listPanel.add(visibilityPanel, getConstraint(1, 0));
        listPanel.add(deletePanel, getConstraint(2, 0));

        deleteAllButton = new JButton("Удалить все");
        deleteAllButton.addActionListener(e -> clear());

        add(listPanel, Constraint.create(0, 0).weightX(1));
        add(new EmptyPanel(), Constraint.create(0, 1).weightX(1).weightY(1));
        add(deleteAllButton, Constraint.create(0, 2).anchor(GridBagConstraints.EAST));

        Log.debug(this, "GUI was created");
    }

    public void add(Model model) {
        if (selectedModel != null) {
            select(selectedModel, false);
            selectedModel = null;
        }

        JLabel label = new JLabel(model.getName());
        label.setForeground(model.getColor());
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedModel != null) {
                    select(selectedModel, false);
                }

                selectedModel = modelItemMap.entrySet().stream()
                        .filter(entry -> entry.getValue().label == label)
                        .map(Map.Entry::getKey)
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Failed to find model by label " + label.getText()));

                select(selectedModel, true);
                eventHandler.invoke(new ModelEvent(model, Status.SELECTED));
            }
        });

        JCheckBox checkBox = new JCheckBox("", true);
        checkBox.addChangeListener(e -> {
            Status status = checkBox.isSelected() ? Status.SHOWED : Status.HID;
            ModelEvent modelEvent = new ModelEvent(model, status);
            eventHandler.invoke(modelEvent);
        });

        JButton deleteButton = new JImageButton(DELETE_IMAGE);
        deleteButton.addActionListener(e -> remove(model));

        listPanel.add(label, getConstraint(0, rowCount + 1));
        listPanel.add(checkBox, getConstraint(1, rowCount + 1).fill(GridBagConstraints.CENTER));
        listPanel.add(deleteButton, getConstraint(2, rowCount + 1).fill(GridBagConstraints.NONE));
        listPanel.updateUI();

        Item item = new Item();
        item.button = deleteButton;
        item.label = label;
        item.checkBox = checkBox;
        modelItemMap.put(model, item);

        rowCount++;
        updateDeleteAllButtonState();

        ModelEvent modelEvent = new ModelEvent(model, Status.ADDED);
        eventHandler.invoke(modelEvent);

        this.updateUI();
    }

    public void addEventListener(EventListener<ModelEvent> listener) {
        eventHandler.add(listener);
    }

    public void removeAddListener(EventListener<ModelEvent> listener) {
        eventHandler.remove(listener);
    }

    private Constraint getConstraint(int gridX, int gridY) {
        return Constraint.create(gridX, gridY)
                .fill(GridBagConstraints.HORIZONTAL)
                .insets(5);
    }

    private void updateDeleteAllButtonState() {
        deleteAllButton.setEnabled(!modelItemMap.isEmpty());
    }

    public boolean remove(Model model) {
        if (selectedModel != null) {
            select(selectedModel, false);
            selectedModel = null;
        }

        Item item = modelItemMap.get(model);
        if (item == null) {
            return false;
        }

        modelItemMap.remove(model);
        listPanel.remove(item.button);
        listPanel.remove(item.checkBox);
        listPanel.remove(item.label);
        listPanel.updateUI();
        updateDeleteAllButtonState();

        ModelEvent modelEvent = new ModelEvent(model, Status.DELETED);
        eventHandler.invoke(modelEvent);
        return true;
    }

    public void clear() {
        List<Model> modelList = modelItemMap.keySet().stream().collect(Collectors.toList());
        for (Model model : modelList) {
            remove(model);
        }
        rowCount = 0;
    }

    public Model getSelectedModel() {
        return selectedModel;
    }

    public void select(Model model) {
        desselect();
        select(model, true);
    }

    public void desselect() {
        select(selectedModel, false);
        selectedModel = null;
    }

    private void select(Model model, boolean select) {
        Item item = modelItemMap.get(model);
        // dirty code
        if (item != null) {
            item.label.setForeground(select ? Color.RED : model.getColor());
            item.label.updateUI();
        } else {
            throw new RuntimeException("Model don't exit: " + model.getName());
        }
    }

    private class Item {
        JLabel label;
        JCheckBox checkBox;
        JButton button;
    }

    public enum Status {
        ADDED,
        DELETED,
        SHOWED,
        HID,
        SELECTED
    }

    public static class ModelEvent {
        private final Status status;
        private final Model model;

        public ModelEvent(Model model, Status status) {
            this.status = status;
            this.model = model;
        }

        public Status getStatus() {
            return status;
        }

        public Model getModel() {
            return model;
        }
    }
}
