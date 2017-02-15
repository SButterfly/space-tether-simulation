package com.sbutterfly.GUI;

import com.sbutterfly.GUI.controls.MultiLineJLabel;
import com.sbutterfly.GUI.controls.MyJTextField;
import com.sbutterfly.GUI.panels.Constraint;
import com.sbutterfly.GUI.panels.JGridBagPanel;
import com.sbutterfly.core.ODEBaseModel;
import com.sbutterfly.differential.EulerODEMethod;
import com.sbutterfly.differential.ODEMethod;
import com.sbutterfly.differential.RungeKuttaODEMethod;
import com.sbutterfly.differential.Vector;
import com.sbutterfly.services.AppSettings;
import com.sbutterfly.utils.DoubleUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Sergei on 01.02.2015.
 */
public class SettingsView implements Frameable {

    private JGridBagPanel panel;
    private JFrame frame;
    private JComboBox<String> comboBox;
    private MyJTextField timeTextField;
    private MyJTextField stepTextField;
    private JButton saveButton;
    private JButton cancelButton;
    private JButton epsButton;
    private JLabel epsLabel;

    private final ODEBaseModel model;

    public SettingsView(ODEBaseModel model) {
        this.model = model;
    }

    public JComponent getComponent() {
        if (panel == null) {
            panel = new JGridBagPanel();

            JLabel methodLabel = new JLabel("Метод интергрирования: ");
            JLabel timeLabel = new JLabel("Время интегрирования, c: ");
            JLabel stepLabel = new JLabel("Шаг интегрирования, c: ");

            comboBox = new JComboBox<>();
            comboBox.addItem("Рунге-Кутты 4 п.т.");
            comboBox.addItem("Эйлера");
            comboBox.setSelectedIndex(0);

            timeTextField = new MyJTextField();
            stepTextField = new MyJTextField();

            JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

            epsButton = new JButton("Оценить погрешность");
            epsButton.addActionListener(e -> startEpsCalc());
            buttonsPanel.add(epsButton);

            epsLabel = new MultiLineJLabel();

            saveButton = new JButton();
            saveButton.setText("Сохранить");
            saveButton.addActionListener(e -> {
                if (save()) {
                    NavigationController.close(this);
                }
            });
            buttonsPanel.add(saveButton);

            cancelButton = new JButton();
            cancelButton.setText("Отмена");
            cancelButton.addActionListener(e -> NavigationController.close(this));
            buttonsPanel.add(cancelButton);

            panel.add(methodLabel, Constraint.create(0, 0).fill(GridBagConstraints.HORIZONTAL).insets(10));
            panel.add(timeLabel, Constraint.create(0, 1).fill(GridBagConstraints.HORIZONTAL).insets(10));
            panel.add(stepLabel, Constraint.create(0, 2).fill(GridBagConstraints.HORIZONTAL).insets(10));

            panel.add(comboBox, Constraint.create(1, 0).fill(GridBagConstraints.HORIZONTAL).insets(10).ipadX(20));
            panel.add(timeTextField, Constraint.create(1, 1).fill(GridBagConstraints.HORIZONTAL).insets(10).ipadX(20));
            panel.add(stepTextField, Constraint.create(1, 2).fill(GridBagConstraints.HORIZONTAL).insets(10).ipadX(20));

            panel.add(buttonsPanel, Constraint.create(0, 3).fill(GridBagConstraints.HORIZONTAL).insets(10).gridWidth(2));
            panel.add(epsLabel, Constraint.create(0, 4).fill(GridBagConstraints.HORIZONTAL).insets(10).gridWidth(2));

            setValues();
        }

        return panel;
    }

    private void startEpsCalc(){

        if (!save()) return;

        epsButton.setEnabled(false);
        epsLabel.setText("Оценка погрешности начата");

        new Thread(() -> {
            try {
                Vector eps = model.getEps(AppSettings.getODEMethod(), AppSettings.getODETime(), AppSettings.getODEStep());

                SwingUtilities.invokeLater(() -> {
                    StringBuilder buff = new StringBuilder();
                    buff.append("Погрешность:\n");
                    buff.append("<table>");
                    for (int i = 0; i < eps.size(); i++) {
                        buff.append(String.format("<tr><td>%s</td><td></td><td>%s</td></tr>", model.paramsNames()[i] + ", " + model.paramsExtNames()[i] + ":", DoubleUtils.toString(eps.get(i))));
                    }
                    buff.append("</table>");
                    epsLabel.setText(buff.toString());
                    epsButton.setEnabled(true);
                });
            }
            catch (Exception e){
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                    epsLabel.setText("");
                    epsButton.setEnabled(true);
                });
            }
        }).start();
    }

    private void setValues() {
        ODEMethod method = AppSettings.getODEMethod();
        double seconds = AppSettings.getODETime();
        double step = AppSettings.getODEStep();

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
        timeTextField.setText(DoubleUtils.toString(seconds));
        stepTextField.setText(DoubleUtils.toString(step));
    }

    private boolean save() {
        try {
            int index = comboBox.getSelectedIndex();
            double seconds = DoubleUtils.parse(timeTextField.getText());
            double step = DoubleUtils.parse(stepTextField.getText());
            ODEMethod method = null;
            if (index == 0) {
                method = new RungeKuttaODEMethod();
            }
            if (index == 1) {
                method = new EulerODEMethod();
            }

            if (seconds <= 0 || step <= 0) {
                throw new NumberFormatException();
            }

            AppSettings.setODEMethod(method);
            AppSettings.setODETime(seconds);
            AppSettings.setODEStep(step);
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Проверьте корректность ввода введенных данных!\nВведенные значения должны быть положительными");
            return false;
        }
    }

    @Override
    public JFrame getFrame() {
        if (frame == null) {
            frame = new JFrame("Настройки");
            frame.getContentPane().add(getComponent());
            frame.pack();
            frame.setSize(400, 300);
            frame.getRootPane().setDefaultButton(saveButton);
        }
        return frame;
    }
}
