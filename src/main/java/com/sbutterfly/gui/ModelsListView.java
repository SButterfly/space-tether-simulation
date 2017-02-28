package com.sbutterfly.gui;

import com.sbutterfly.engine.Model;
import com.sbutterfly.gui.controls.EmptyPanel;
import com.sbutterfly.gui.controls.JImageButton;
import com.sbutterfly.gui.helpers.EventHandler;
import com.sbutterfly.gui.helpers.EventListener;
import com.sbutterfly.gui.panels.Constraint;
import com.sbutterfly.gui.panels.JGridBagPanel;
import com.sbutterfly.utils.Log;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Sergei on 14.02.2015.
 */
public class ModelsListView extends JGridBagPanel {

    private static final URL DELETE_IMAGE = ModelsListView.class
            .getClassLoader().getResource("delete.png");

    private JGridBagPanel listPanel;

    private ArrayList<Item> list = new ArrayList<>();
    private int itemsCount = 0;
    private JButton deleteAllButton;
    private EventHandler<ModelEvent> eventHandler = new EventHandler<>();

    public ModelsListView() {
        createGUI();
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
        JLabel label = new JLabel(model.getName());
        label.setForeground(model.getColor());
        JCheckBox checkBox = new JCheckBox("", true);
        checkBox.addChangeListener(e -> {
            Status status = checkBox.isSelected() ? Status.SHOWED : Status.HID;
            ModelEvent modelEvent = new ModelEvent(model, status);
            eventHandler.invoke(modelEvent);
        });

        JButton deleteButton = new JImageButton(DELETE_IMAGE);
        deleteButton.addActionListener(e -> remove(model));

        int row = list.size();
        listPanel.add(label, getConstraint(0, row + 1));
        listPanel.add(checkBox, getConstraint(1, row + 1).fill(GridBagConstraints.CENTER));
        listPanel.add(deleteButton, getConstraint(2, row + 1).fill(GridBagConstraints.NONE));
        listPanel.updateUI();

        Item item = new Item();
        item.button = deleteButton;
        item.label = label;
        item.checkBox = checkBox;
        item.model = model;
        list.add(item);

        itemsCount++;
        updateDeleteAllButtonState();

        ModelEvent modelEvent = new ModelEvent(model, Status.ADDED);
        eventHandler.invoke(modelEvent);
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
        deleteAllButton.setEnabled(itemsCount != 0);
    }

    public boolean remove(Model model) {
        Item item = list.stream().filter(i -> i.model == model).findFirst().orElse(null);
        if (item == null) {
            return false;
        }

        listPanel.remove(item.button);
        listPanel.remove(item.checkBox);
        listPanel.remove(item.label);
        itemsCount--;
        updateDeleteAllButtonState();

        ModelEvent modelEvent = new ModelEvent(item.model, Status.DELETED);
        eventHandler.invoke(modelEvent);
        return true;
    }

    public void clear() {
        for (Item item : list) {
            remove(item.model);
        }
    }

    private class Item {
        JLabel label;
        JCheckBox checkBox;
        JButton button;
        Model model;
    }

    public enum Status {
        SHOWED,
        HID,
        DELETED,
        ADDED
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
