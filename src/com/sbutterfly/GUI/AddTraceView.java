package com.sbutterfly.GUI;

import com.sbutterfly.GUI.controls.MyJTextField;
import com.sbutterfly.GUI.panels.Constraint;
import com.sbutterfly.GUI.panels.JGridBagPanel;
import com.sbutterfly.core.ODEBaseModel;
import com.sbutterfly.differential.Index;
import com.sbutterfly.utils.Log;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Sergei on 13.02.2015.
 */
public class AddTraceView extends JGridBagPanel {

    private JComboBox<String> yComboBox;
    private JComboBox<String> xComboBox;
    private JTextField nameTextField;
    private JButton submitButton;

    private ODEBaseModel model;
    private Index[] indexes;
    private ArrayList<SubmitListener<Traceable>> listeners = new ArrayList<>();

    public AddTraceView() {
        createGUI();
        setEnabled(false);
    }

    private static String getName(String yName, String xName) {
        return yName + "(" + xName + ")";
    }

    private void createGUI(){

        JLabel ylabel = new JLabel("Ось ординат:");
        JLabel xlabel = new JLabel("Ось абсцисс:");
        JLabel namelabel = new JLabel("Название:");

        yComboBox = new JComboBox<>();
        yComboBox.addActionListener(e -> OnSelectionChanged());
        xComboBox = new JComboBox<>();
        xComboBox.addActionListener(e -> OnSelectionChanged());
        nameTextField = new MyJTextField();

        submitButton = new JButton("Добавить");
        submitButton.addActionListener(e -> OnAdd());

        add(ylabel, getConstraint(0, 0));
        add(xlabel, getConstraint(0, 1));
        add(namelabel, getConstraint(0, 2));

        add(yComboBox, getConstraint(1, 0).weightX(1));
        add(xComboBox, getConstraint(1, 1).weightX(1));
        add(nameTextField, getConstraint(1, 2).weightX(1));

        add(submitButton, getConstraint(0, 3).gridWidth(2).fill(GridBagConstraints.NONE).anchor(GridBagConstraints.EAST));

        Log.debug(this, "GUI was created");
    }

    @Override
    public void setEnabled(boolean value) {
        super.setEnabled(value);
        yComboBox.setEnabled(value);
        xComboBox.setEnabled(value);
        nameTextField.setEnabled(value);
        submitButton.setEnabled(value);
    }

    public void Init(ODEBaseModel model){
        this.setEnabled(true);
        this.model = model;
        int lastXSelected = xComboBox.getSelectedIndex();
        int lastYSelected = yComboBox.getSelectedIndex();
        lastXSelected = Math.max(lastXSelected, 0);
        lastYSelected = Math.max(lastYSelected, 0);

        yComboBox.removeAllItems();
        xComboBox.removeAllItems();
        setEnabled(true);

        String[] names = model.paramsNames();
        String[] customNames = model.customParamsNames();

        indexes = new Index[names.length + customNames.length];

        int i = 0;
        for (; i < names.length; i++) {
            if (names[i] != null){
                yComboBox.addItem(names[i]);
                xComboBox.addItem(names[i]);
            }
            indexes[i] = new Index(i);
        }

        for (int j = 0; i < names.length + customNames.length; i++, j++) {
            if (customNames[j] != null) {
                yComboBox.addItem(customNames[j]);
                xComboBox.addItem(customNames[j]);
            }
            indexes[i] = new Index(j, model.getCustomable(j));
        }

        xComboBox.setSelectedIndex(Math.min(lastXSelected, xComboBox.getItemCount() - 1));
        yComboBox.setSelectedIndex(Math.min(lastYSelected, yComboBox.getItemCount() - 1));
    }

    public void OnSelectionChanged() {
        String xName = (String) xComboBox.getSelectedItem();
        String yName = (String) yComboBox.getSelectedItem();

        String name = getName(yName, xName);
        nameTextField.setText(name);
    }

    private void OnAdd(){

        int xIndex = xComboBox.getSelectedIndex();
        int yIndex = yComboBox.getSelectedIndex();

        String name = nameTextField.getText();

        for (SubmitListener<Traceable> listener : listeners){
            Traceable traceable = new Traceable();
            traceable.name = name;
            traceable.xIndex = indexes[xIndex];
            traceable.yIndex = indexes[yIndex];

            listener.onSubmit(traceable);
        }
    }

    public void addSubmitListener(SubmitListener<Traceable> listener){
        listeners.add(listener);
    }

    public void removeSubmitListener(SubmitListener<Traceable> listener){
        listeners.remove(listener);
    }

    private Constraint getConstraint(int gridX, int gridY) {
        return Constraint.create(gridX, gridY).fill(GridBagConstraints.HORIZONTAL).insets(3, 5);
    }

    public class Traceable {
        Index yIndex;
        Index xIndex;
        String name;
    }
}
