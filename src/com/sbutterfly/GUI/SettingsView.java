package com.sbutterfly.GUI;

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

    private JPanel panel;
    private JFrame frame;
    private JComboBox<String> comboBox;
    private JTextField timeTextField;

    public JComponent getComponent() {
        if (panel == null) {
            panel = new JPanel();
            panel.setLayout(new GridBagLayout());

            JLabel methodLabel = new JLabel();
            methodLabel.setText("Методы интергрирования");

            JLabel timeLabel = new JLabel();
            timeLabel.setText("Время интегрирования");

            comboBox = new JComboBox<>();
            comboBox.addItem("Рунге-Кутты 4 п.т.");
            comboBox.addItem("Эйлера");
            comboBox.setSelectedIndex(0);

            timeTextField = new JTextField();

            JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton saveButton = new JButton();
            saveButton.setText("Сохранить");
            saveButton.addActionListener(e -> {
                save();
                NavigationController.Close(this);
            });
            buttonsPanel.add(saveButton);

            JButton cancelButton = new JButton();
            cancelButton.setText("Отмена");
            cancelButton.addActionListener(e -> NavigationController.Close(this));
            buttonsPanel.add(cancelButton);

            GridBagConstraints c = new GridBagConstraints();

            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(10, 10, 10, 10);
            panel.add(methodLabel, c);

            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 1;
            c.gridy = 0;
            c.insets = new Insets(10, 10, 10, 10);
            panel.add(comboBox, c);

            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy = 1;
            c.insets = new Insets(10, 10, 10, 10);
            panel.add(timeLabel, c);

            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 1;
            c.gridy = 1;
            c.insets = new Insets(10, 10, 10, 10);
            c.ipadx = 100;
            panel.add(timeTextField, c);

            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy = 2;
            c.gridwidth = 2;
            c.insets = new Insets(20, 10, 10, 10);
            c.ipadx = 100;
            panel.add(buttonsPanel, c);

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
        timeTextField.setText(seconds + " ");
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
        }
        return frame;
    }
}
