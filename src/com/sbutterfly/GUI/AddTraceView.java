package com.sbutterfly.GUI;

import com.sbutterfly.differential.Index;
import com.sbutterfly.differential.ODEBaseModel;
import com.sbutterfly.helpers.Log;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Sergei on 13.02.2015.
 */
public class AddTraceView extends JPanel {

    private JComboBox<String> yComboBox;
    private JComboBox<String> xComboBox;
    private JTextField nameTextField;
    private JButton submitButton;

    private ODEBaseModel model;
    private int[] nameIndexes;

    public AddTraceView() {
        createGUI();
        setEnabled(false);
    }

    private void createGUI(){
        setLayout(new GridBagLayout());

        JLabel ylabel = new JLabel("Ось ординат:");
        JLabel xlabel = new JLabel("Ось абсцис:");
        JLabel namelabel = new JLabel("Название:");

        yComboBox = new JComboBox<>();
        yComboBox.addActionListener(e -> OnSelectionChanged());
        xComboBox = new JComboBox<>();
        xComboBox.addActionListener(e -> OnSelectionChanged());
        nameTextField = new JTextField();

        submitButton = new JButton("Добавить");
        submitButton.addActionListener(e -> OnAdd());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(0, 10, 0, 10);
        c.gridx = 0;
        c.gridy = 0;
        add(ylabel, c);
        c.gridy = 1;
        add(xlabel, c);
        c.gridy = 2;
        add(namelabel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(0, 10, 0, 10);
        c.gridx = 1;
        c.gridy = 0;
        add(yComboBox, c);
        c.gridy = 1;
        add(xComboBox, c);
        c.gridy = 2;
        add(nameTextField, c);

        c.gridy = 3;
        c.gridx = 1;
        c.weightx = 2;
        add(submitButton, c);

        Log.Debug(this, "GUI was created");
    }

    public void Init(ODEBaseModel model){
        this.model = model;
        yComboBox.removeAllItems();
        xComboBox.removeAllItems();
        setEnabled(true);

        nameIndexes = new int[model.size()+1];
        String[] names = model.names();

        nameIndexes[0] = -1;
        yComboBox.addItem("time");
        xComboBox.addItem("time");

        for (int i = 0, j = 1; i < names.length; i++){
            if (names[i] != null){
                nameIndexes[j++] = i;
                yComboBox.addItem(names[i]);
                xComboBox.addItem(names[i]);
            }
        }
    }

    public void OnSelectionChanged() {
        String xName = (String) xComboBox.getSelectedItem();
        String yName = (String) yComboBox.getSelectedItem();

        String name = getName(yName, xName);
        nameTextField.setText(name);
    }

    private void OnAdd(){

        int xIndex = nameIndexes[xComboBox.getSelectedIndex()];
        int yIndex = nameIndexes[yComboBox.getSelectedIndex()];

        String name = nameTextField.getText();

        for (SubmitListener<Traceable> listener : listeners){
            Traceable traceable = new Traceable();
            traceable.name = name;
            traceable.xIndex = Index.toIndex(xIndex);
            traceable.yIndex = Index.toIndex(yIndex);
            listener.OnSubmit(traceable);
        }
    }

    private static String getName(String yName, String xName){
        return yName + "(" + xName + ")";
    }

    private ArrayList<SubmitListener<Traceable>> listeners = new ArrayList<>();
    public void addSubmitListener(SubmitListener<Traceable> listener){
        listeners.add(listener);
    }
    public void removeSubmitListener(SubmitListener<Traceable> listener){
        listeners.remove(listener);
    }

    public class Traceable {
        Index yIndex;
        Index xIndex;
        String name;
    }
}
