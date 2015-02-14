package com.sbutterfly.GUI;

import com.sbutterfly.differential.ODEBaseModel;
import com.sbutterfly.helpers.Log;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Sergei on 02.02.2015.
 */
public class InitialStateView extends JPanel {

    private ODEBaseModel model;

    public InitialStateView(ODEBaseModel model)
    {
        this.model = model;
        createGUI();
    }

    private void createGUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel headerLabel = new JLabel();
        headerLabel.setText("Начальные параметры");

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        JButton submitButton = new JButton();
        submitButton.setText("Расчитать");
        submitButton.addActionListener(e -> {
            for (SubmitListener<ODEBaseModel> listener : list) {
                listener.OnSubmit(model);
            }
        });

        setAdditionalParams(panel, submitButton);
        setStartParams(panel, submitButton);

        add(headerLabel);
        add(panel);
        add(submitButton);

        Log.Debug(this, "GUI was created");
    }

    private int lastRow = 0;

    private void setAdditionalParams(JPanel panel, JButton saveButton) {

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

    private void setStartParams(JPanel panel, JButton saveButton) {

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

    private void setParams(JPanel panel, JButton saveButton, String[] names, double[] values, Settable[] settable){
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

                JTextField textField = new JTextField(valueList.get(i) + "");
                panel.add(textField, getConstraint(1, lastRow, 1 + (i == n-1 ? 2 : 0), 1));

                final int finalI = i;
                saveButton.addActionListener(e -> settables.get(finalI).OnSet(Double.parseDouble(textField.getText())));
            }

            if (i + 1 < n) {

                JLabel label = new JLabel(nameList.get(i + 1) + ":");
                panel.add(label, getConstraint(2, lastRow, 1, 1));

                JTextField textField = new JTextField(valueList.get(i + 1) + "");
                panel.add(textField, getConstraint(3, lastRow, 1, 1));

                final int finalI = i + 1;
                saveButton.addActionListener(e -> settables.get(finalI).OnSet(Double.parseDouble(textField.getText())));
            }
        }
    }

    private ArrayList<SubmitListener<ODEBaseModel>> list = new ArrayList<>();
    public void addSubmitListener(SubmitListener<ODEBaseModel> listener){
        list.add(listener);
    }
    public void removeSubmitListener(SubmitListener<ODEBaseModel> listener){
        list.remove(listener);
    }

    private static interface Settable {
        void OnSet(double value);
    }

    private GridBagConstraints getConstraint(int gridX, int gridY, int gridwidth, int gridheight){
        GridBagConstraints c = new GridBagConstraints();
        //c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = gridX;
        c.gridy = gridY;
        c.anchor = GridBagConstraints.WEST;
        c.gridheight = gridheight;
        c.gridwidth = gridwidth;
        c.insets = new Insets(0, 10, 0, 10);
        return c;
    }
}

