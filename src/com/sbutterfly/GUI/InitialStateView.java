package com.sbutterfly.GUI;

import com.sbutterfly.GUI.Controls.MyJTextField;
import com.sbutterfly.GUI.Panels.Constraint;
import com.sbutterfly.GUI.Panels.JGridBagPanel;
import com.sbutterfly.differential.ODEBaseModel;
import com.sbutterfly.helpers.Log;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Sergei on 02.02.2015.
 */
public class InitialStateView extends JGridBagPanel {

    private ODEBaseModel model;
    private int lastRow = 0;
    private ArrayList<SubmitListener<ODEBaseModel>> list = new ArrayList<>();

    public InitialStateView(ODEBaseModel model)
    {
        this.model = model;
        createGUI();
    }

    private void createGUI() {

        JLabel headerLabel = new JLabel();
        headerLabel.setText("Начальные параметры");

        JButton submitButton = new JButton();
        submitButton.setText("Расчитать");
        submitButton.addActionListener(e -> {
            for (SubmitListener<ODEBaseModel> listener : list) {
                listener.OnSubmit(model);
            }
        });

        add(headerLabel, getConstraint(0, lastRow++, 1, 1).gridWidth(4));

        setAdditionalParams(this, submitButton);
        setStartParams(this, submitButton);

        add(submitButton, getConstraint(0, lastRow, 4, 1).fill(GridBagConstraints.NONE).anchor(GridBagConstraints.EAST));

        Log.debug(this, "GUI was created");
    }

    private void setAdditionalParams(JGridBagPanel panel, JButton saveButton) {

        double[] values = new double[model.additionalSize()];
        Settable[] settable = new Settable[model.additionalSize()];
        String[] names = model.additionalNames();

        for (int i = 0; i < model.additionalSize(); i++){
            final int finalI = i;
            values[finalI] = model.getAdditionalParameter(finalI);
            settable[finalI] = value -> model.setAdditionalParameter(finalI, value);
        }

        setParams(panel, saveButton, names, values, settable);
    }

    private void setStartParams(JGridBagPanel panel, JButton saveButton) {

        double[] values = new double[model.size()];
        Settable[] settable = new Settable[model.size()];
        String[] names = model.names();

        for (int i = 0; i < model.size(); i++){
            final int finalI = i;
            values[finalI] = model.getStartParameter(finalI);
            settable[finalI] = value -> model.setStartParameter(finalI, value);
        }

        setParams(panel, saveButton, names, values, settable);
    }

    private void setParams(JGridBagPanel panel, JButton saveButton, String[] names, double[] values, Settable[] settable) {
        ArrayList<String> nameList = new ArrayList<>(names.length);
        ArrayList<Double> valueList = new ArrayList<>(names.length);
        ArrayList<Settable> settables = new ArrayList<>(names.length);

        for (int i = 0, n = names.length; i < n; i++){
            if (names[i] != null && settable[i] != null){
                nameList.add(names[i]);
                valueList.add(values[i]);
                settables.add(settable[i]);
            }
        }

        for (int i = 0, n = settables.size(); i < n; i+=2, lastRow++){

            if (i < n) {

                JLabel label = new JLabel(nameList.get(i) + ":");
                panel.add(label, getConstraint(0, lastRow, 1, 1));

                JTextField textField = new MyJTextField(valueList.get(i) + "");
                panel.add(textField, getConstraint(1, lastRow, 1 + (i == n - 1 ? 2 : 0), 1));

                final int finalI = i;
                saveButton.addActionListener(e -> settables.get(finalI).OnSet(Double.parseDouble(textField.getText())));
            }

            if (i + 1 < n) {

                JLabel label = new JLabel(nameList.get(i + 1) + ":");
                panel.add(label, getConstraint(2, lastRow, 1, 1));

                JTextField textField = new MyJTextField(valueList.get(i + 1) + "");
                panel.add(textField, getConstraint(3, lastRow, 1, 1));

                final int finalI = i + 1;
                saveButton.addActionListener(e -> settables.get(finalI).OnSet(Double.parseDouble(textField.getText())));
            }
        }
    }

    public void addSubmitListener(SubmitListener<ODEBaseModel> listener){
        list.add(listener);
    }
    public void removeSubmitListener(SubmitListener<ODEBaseModel> listener){
        list.remove(listener);
    }

    private Constraint getConstraint(int gridX, int gridY, int gridWidth, int gridHeight) {
        return Constraint.create(gridX, gridY, gridWidth, gridHeight)
                .fill(GridBagConstraints.HORIZONTAL)
                .weightX(1)
                .insets(3, 5);
    }

    private static interface Settable {
        void OnSet(double value);
    }
}

