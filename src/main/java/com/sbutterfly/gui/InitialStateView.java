package com.sbutterfly.gui;

import com.sbutterfly.gui.controls.EmptyPanel;
import com.sbutterfly.gui.controls.MyJTextField;
import com.sbutterfly.gui.panels.Constraint;
import com.sbutterfly.gui.panels.JGridBagPanel;
import com.sbutterfly.core.BaseSystem;
import com.sbutterfly.utils.DoubleUtils;
import com.sbutterfly.utils.Log;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Sergei on 02.02.2015.
 */
public class InitialStateView extends JGridBagPanel {

    protected ArrayList<SubmitListener<BaseSystem>> list = new ArrayList<>();
    private BaseSystem model;
    private int lastRow = 0;

    public InitialStateView(BaseSystem model) {
        this.model = model;
        createGUI();
    }

    public BaseSystem getModel() {
        return model;
    }

    protected void createGUI() {

        JLabel headerLabel = new JLabel();
        headerLabel.setText("Начальные параметры:");

        JButton submitButton = new JButton();
        submitButton.setText("Рассчитать");
        submitButton.addActionListener(e -> {
            for (SubmitListener<BaseSystem> listener : list) {
                listener.onSubmit(model);
            }
        });

        add(headerLabel, getConstraint(0, lastRow++, 1, 1).gridWidth(4));

        setAdditionalParams(this, submitButton);

        add(new EmptyPanel(), getConstraint(0, lastRow++, 1, 1).gridWidth(4));

        setStartParams(this, submitButton);

        add(submitButton, getConstraint(0, lastRow, 4, 1).fill(GridBagConstraints.NONE).anchor(GridBagConstraints.EAST));

        Log.debug(this, "GUI was created");
    }

    private void setAdditionalParams(JGridBagPanel panel, JButton saveButton) {

        int size = model.initialParamsNames().length;
        double[] values = new double[size];
        Settable[] settable = new Settable[size];
        String[] names = model.initialParamsNames();

        for (int i = 0; i < size; i++) {
            final int finalI = i;
            values[finalI] = model.getInitialParameter(finalI);
            settable[finalI] = value -> model.setInitialParameter(finalI, value);
        }

        setParams(panel, saveButton, names, values, settable);
    }

    private void setStartParams(JGridBagPanel panel, JButton saveButton) {
        int size = model.paramsNames().length;
        double[] values = new double[size];
        Settable[] settable = new Settable[size];
        String[] names = model.paramsNames();

        for (int i = 0; i < size; i++) {
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

        for (int i = 0, n = names.length; i < n; i++) {
            if (names[i] != null && settable[i] != null) {
                nameList.add(names[i]);
                valueList.add(values[i]);
                settables.add(settable[i]);
            }
        }

        for (int i = 0, n = settables.size(); i < n; i += 2, lastRow++) {

            if (i < n) {

                JLabel label = new JLabel(nameList.get(i) + ":");
                panel.add(label, getConstraint(0, lastRow, 1, 1));

                JTextField textField = new MyJTextField(DoubleUtils.toString(valueList.get(i)));
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

    public void addSubmitListener(SubmitListener<BaseSystem> listener) {
        list.add(listener);
    }

    public void removeSubmitListener(SubmitListener<BaseSystem> listener) {
        list.remove(listener);
    }

    private Constraint getConstraint(int gridX, int gridY, int gridWidth, int gridHeight) {
        return Constraint.create(gridX, gridY, gridWidth, gridHeight)
            .fill(GridBagConstraints.HORIZONTAL)
            .weightX(1)
            .insets(3, 5);
    }

    protected interface Settable {
        void OnSet(double value);
    }
}

