package com.sbutterfly.GUI;

import com.sbutterfly.GUI.Controls.MyJTextField;
import com.sbutterfly.GUI.Panels.Constraint;
import com.sbutterfly.GUI.Panels.JGridBagPanel;
import com.sbutterfly.differential.EulerODEMethod;
import com.sbutterfly.differential.ODEMethod;
import com.sbutterfly.differential.RungeKuttaODEMethod;
import com.sbutterfly.services.AppSettings;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Sergei on 01.02.2015.
 */
public class SettingsView implements Frameable {

    private JGridBagPanel panel;
    private JFrame frame;
    private JComboBox<String> comboBox;
    private JTextField timeTextField;
    private JButton saveButton;
    private JButton cancelButton;

    public JComponent getComponent() {
        if (panel == null) {
            panel = new JGridBagPanel();

            JLabel methodLabel = new JLabel();
            methodLabel.setText("Методы интергрирования");

            JLabel timeLabel = new JLabel();
            timeLabel.setText("Время интегрирования");

            comboBox = new JComboBox<>();
            comboBox.addItem("Рунге-Кутты 4 п.т.");
            comboBox.addItem("Эйлера");
            comboBox.setSelectedIndex(0);

            timeTextField = new MyJTextField();

            JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            saveButton = new JButton();
            saveButton.setText("Сохранить");
            saveButton.addActionListener(e -> {
                save();
                NavigationController.Close(this);
            });
            buttonsPanel.add(saveButton);

            cancelButton = new JButton();
            cancelButton.setText("Отмена");
            cancelButton.addActionListener(e -> NavigationController.Close(this));
            buttonsPanel.add(cancelButton);

            panel.add(methodLabel, Constraint.create(0, 0).fill(GridBagConstraints.HORIZONTAL).insets(10));
            panel.add(timeLabel, Constraint.create(0, 1).fill(GridBagConstraints.HORIZONTAL).insets(10));
            panel.add(comboBox, Constraint.create(1, 0).fill(GridBagConstraints.HORIZONTAL).insets(10).ipadX(20));
            panel.add(timeTextField, Constraint.create(1, 1).fill(GridBagConstraints.HORIZONTAL).insets(10).ipadX(20));

            panel.add(buttonsPanel, Constraint.create(0, 2).fill(GridBagConstraints.HORIZONTAL).insets(10).gridWidth(2));

            setValues();
        }

        return panel;
    }

    private void setValues() {
        ODEMethod method = AppSettings.getODEMethod();
        double seconds = AppSettings.getODETime();

        int index = -1;
        if (method instanceof RungeKuttaODEMethod){
            index = 0;
        }
        if (method instanceof EulerODEMethod){
            index = 1;
        }

        if (index == -1){
            throw new RuntimeException("No implemented index for " + method);
        }

        comboBox.setSelectedIndex(index);
        timeTextField.setText(seconds + "");
    }

    private void save() {
        int index = comboBox.getSelectedIndex();
        double seconds = Double.parseDouble(timeTextField.getText());
        ODEMethod method = null;
        if (index == 0){
            method = new RungeKuttaODEMethod();
        }
        if (index == 1){
            method = new EulerODEMethod();
        }

        AppSettings.setODEMethod(method);
        AppSettings.setODETime(seconds);
    }

    @Override
    public JFrame getFrame() {
        if (frame == null) {
            frame = new JFrame("Настройки");
            frame.getContentPane().add(getComponent());
            frame.pack();
            frame.setSize(400, 200);
            frame.getRootPane().setDefaultButton(saveButton);
        }
        return frame;
    }
}
