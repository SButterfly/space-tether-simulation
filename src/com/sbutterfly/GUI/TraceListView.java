package com.sbutterfly.GUI;

import com.sbutterfly.GUI.controls.EmptyPanel;
import com.sbutterfly.GUI.controls.JImageButton;
import com.sbutterfly.GUI.panels.Constraint;
import com.sbutterfly.GUI.panels.JGridBagPanel;
import com.sbutterfly.utils.Log;
import info.monitorenter.gui.chart.ITrace2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Sergei on 14.02.2015.
 */
public class TraceListView extends JGridBagPanel {

    private JGridBagPanel listPanel;

    private ArrayList<Item> list = new ArrayList<>();
    private int itemsCount = 0;
    private JButton deleteAllButton;
    private ArrayList<ActionListener> addListeners = new ArrayList<>();
    private ArrayList<SubmitListener<ITrace2D>> removeListeners = new ArrayList<>();
    private ArrayList<ActionListener> removeAllListeners = new ArrayList<>();

    public TraceListView() {
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

    public void Add(ITrace2D trace){

        JLabel label = new JLabel(trace.getName());
        JCheckBox checkBox = new JCheckBox("", true);
        checkBox.addChangeListener(e -> trace.setVisible(checkBox.isSelected()));
        JButton button = new JImageButton("assets/delete.png");
        button.addActionListener(e -> {
            listPanel.remove(label);
            listPanel.remove(checkBox);
            listPanel.remove(button);
            listPanel.updateUI();
            itemsCount--;
            updateDeleteAllButtonState();
            for (SubmitListener<ITrace2D> listener : removeListeners){
                listener.onSubmit(trace);
            }
        });

        int row = list.size();
        trace.setColor(getColor(row));
        label.setForeground(trace.getColor());
        listPanel.add(label, getConstraint(0, row+1));
        listPanel.add(checkBox, getConstraint(1, row + 1).fill(GridBagConstraints.CENTER));
        listPanel.add(button, getConstraint(2, row + 1).fill(GridBagConstraints.NONE));

        Item item = new Item();
        item.button = button;
        item.label = label;
        item.checkBox = checkBox;
        item.trace2D = trace;

        listPanel.updateUI();

        list.add(item);
        itemsCount++;
        updateDeleteAllButtonState();

        for (ActionListener listener : addListeners){
            listener.actionPerformed(new ActionEvent(this, 0, "add"));
        }
    }

    private Color getColor(int i){
        i %= 5;
        if (i == 0) return Color.BLUE;
        if (i == 1) return Color.RED;
        if (i == 2) return Color.CYAN;
        if (i == 3) return Color.GREEN;
        return Color.DARK_GRAY;
    }

    public void addAddListener(ActionListener listener){
        addListeners.add(listener);
    }

    public void removeAddListener(ActionListener listener){
        addListeners.remove(listener);
    }

    public void addRemoveListener(SubmitListener<ITrace2D> listener){
        removeListeners.add(listener);
    }

    public void removeRemoveListener(SubmitListener<ITrace2D> listener){
        removeListeners.remove(listener);
    }

    public void addRemoveAllListener(ActionListener listener){
        removeAllListeners.add(listener);
    }
    public void removeRemoveAllListener(ActionListener listener){
        removeAllListeners.remove(listener);
    }

    private Constraint getConstraint(int gridX, int gridY) {
        return Constraint.create(gridX, gridY)
                .fill(GridBagConstraints.HORIZONTAL)
                .insets(5);
    }

    private void updateDeleteAllButtonState() {
        deleteAllButton.setEnabled(itemsCount != 0);
    }

    public void clear() {
        for (Item item : list) {
            listPanel.remove(item.button);
            listPanel.remove(item.checkBox);
            listPanel.remove(item.label);
        }
        list.clear();
        itemsCount = 0;
        listPanel.updateUI();
        updateDeleteAllButtonState();
        for (ActionListener listener : removeAllListeners) {
            listener.actionPerformed(new ActionEvent(this, 0, "removeAll"));
        }
    }

    private class Item {
        JLabel label;
        JCheckBox checkBox;
        JButton button;
        ITrace2D trace2D;
    }
}
