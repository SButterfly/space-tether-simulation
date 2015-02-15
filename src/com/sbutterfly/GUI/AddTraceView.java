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
        nameTextField = new MyJTextField();

        submitButton = new JButton("Добавить");
        submitButton.addActionListener(e -> OnAdd());

        add(ylabel, getConstraint(0, 0).get());
        add(xlabel, getConstraint(0, 1).get());
        add(namelabel, getConstraint(0, 2).get());

        add(yComboBox, getConstraint(1, 0).weightX(1).get());
        add(xComboBox, getConstraint(1, 1).weightX(1).get());
        add(nameTextField, getConstraint(1, 2).weightX(1).get());

        add(submitButton, getConstraint(0, 3).gridWidth(2).fill(GridBagConstraints.NONE).anchor(GridBagConstraints.EAST).get());

        Log.Debug(this, "GUI was created");
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

    private Constraint getConstraint(int gridX, int gridY){
        return Constraint.New(gridX, gridY).fill(GridBagConstraints.HORIZONTAL).insets(3, 5);
    }
}
