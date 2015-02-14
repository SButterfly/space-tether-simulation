package com.sbutterfly.GUI;

import com.sbutterfly.helpers.Log;
import info.monitorenter.gui.chart.ITrace2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Sergei on 14.02.2015.
 */
public class TraceListView extends JPanel {

    private JPanel listPanel;

    private ArrayList<Item> list = new ArrayList<>();

    public TraceListView() {
        createGUI();
    }

    private void createGUI(){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        listPanel = new JPanel();
        listPanel.setLayout(new GridBagLayout());

        JLabel namePanel = new JLabel("Название");
        JLabel visibilityPanel = new JLabel("Видимость");
        JLabel deletePanel = new JLabel("Удалить");

        listPanel.add(namePanel, getConstraint(0, 0));
        listPanel.add(visibilityPanel, getConstraint(1, 0));
        listPanel.add(deletePanel, getConstraint(2, 0));

        JButton jButton = new JButton("Удалить все");
        jButton.addActionListener(e -> {
            for (Item item : list){
                listPanel.remove(item.button);
                listPanel.remove(item.checkBox);
                listPanel.remove(item.label);
            }
            list.clear();
            listPanel.updateUI();
            for (ActionListener listener : removeAllListeners){
                listener.actionPerformed(e);
            }
        });

        add(listPanel);
        add(jButton);

        Log.Debug(this, "GUI was created");
    }

    public void Add(ITrace2D trace){

        JLabel label = new JLabel(trace.getName());
        JCheckBox checkBox = new JCheckBox("", true);
        checkBox.addChangeListener(e -> {
            boolean vis = checkBox.isVisible();
            trace.setVisible(vis);
        });
        JButton button = new JButton("DEL");
        button.addActionListener(e -> {
            listPanel.remove(label);
            listPanel.remove(checkBox);
            listPanel.remove(button);
            listPanel.updateUI();

            for (SubmitListener<ITrace2D> listener : removeListeners){
                listener.OnSubmit(trace);
            }
        });

        int row = list.size();
        trace.setColor(getColor(row));
        listPanel.add(label, getConstraint(0, row+1));
        listPanel.add(checkBox, getConstraint(1, row+1));
        listPanel.add(button, getConstraint(2, row+1));

        Item item = new Item();
        item.button = button;
        item.label = label;
        item.checkBox = checkBox;
        item.trace2D = trace;

        listPanel.updateUI();

        list.add(item);

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
        return Color.YELLOW;
    }

    private ArrayList<ActionListener> addListeners = new ArrayList<>();
    public void addAddListener(ActionListener listener){
        addListeners.add(listener);
    }
    public void removeAddListener(ActionListener listener){
        addListeners.remove(listener);
    }

    private ArrayList<SubmitListener<ITrace2D>> removeListeners = new ArrayList<>();
    public void addRemoveListener(SubmitListener<ITrace2D> listener){
        removeListeners.add(listener);
    }
    public void removeRemoveListener(SubmitListener<ITrace2D> listener){
        removeListeners.remove(listener);
    }

    private ArrayList<ActionListener> removeAllListeners = new ArrayList<>();
    public void addRemoveAllListener(ActionListener listener){
        removeAllListeners.add(listener);
    }
    public void removeRemoveAllListener(ActionListener listener){
        removeAllListeners.remove(listener);
    }

    private class Item {
        JLabel label;
        JCheckBox checkBox;
        JButton button;
        ITrace2D trace2D;
    }

    private GridBagConstraints getConstraint(int gridX, int gridY){
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = gridX;
        c.gridy = gridY;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(5, 5, 5, 5);

        return c;
    }
}
