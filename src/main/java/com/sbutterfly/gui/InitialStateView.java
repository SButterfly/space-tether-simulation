package com.sbutterfly.gui;

import com.sbutterfly.engine.GroupAxisDescription;
import com.sbutterfly.engine.Model;
import com.sbutterfly.engine.trace.Axis;
import com.sbutterfly.gui.controls.EmptyPanel;
import com.sbutterfly.gui.controls.MyJTextField;
import com.sbutterfly.gui.panels.Constraint;
import com.sbutterfly.gui.panels.JGridBagPanel;
import com.sbutterfly.utils.DoubleUtils;
import com.sbutterfly.utils.Log;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Sergei on 02.02.2015.
 */
public class InitialStateView extends JGridBagPanel {

    private final ArrayList<SubmitListener<Event>> listeners = new ArrayList<>();

    // Используется String вместо Double, чтобы валидация происходила в самом конце перед сохранением.
    private final Map<Axis, String> params = new HashMap<>();

    private Model model;
    private State state;

    private int lastRow = 0;

    public InitialStateView() {
    }

    public Model getModel() {
        return model;
    }

    public State getState() {
        return state;
    }

    public void setModel(Model model, State state) {
        this.model = model;
        this.state = state;
        clear();
        createGUI();
    }

    private void createGUI() {
        List<GroupAxisDescription> list = model.getModelDescription();
        for (GroupAxisDescription groupAxisDescription : list) {
            addGroupName(groupAxisDescription.getName());
            for (Axis axis : groupAxisDescription) {
                addAxis(axis, DoubleUtils.toString(model.getInitialValue(axis)));
            }
        }

        add(new EmptyPanel(), getConstraint(0, lastRow++, 1, 1).gridWidth(2));

        if (state == State.CREATE) {
            JButton submitButton = new JButton();
            submitButton.setText("Добавить");
            add(submitButton, getConstraint(0, lastRow, 2, 1)
                    .fill(GridBagConstraints.NONE)
                    .anchor(GridBagConstraints.EAST));
            submitButton.addActionListener(l -> submitChanges());
        } else {
            // TODO
            throw new UnsupportedOperationException("Unsupported state");
        }

        Log.debug(this, "GUI updated");
    }

    private void clear() {
        removeAll();
        params.clear();
    }

    private void addGroupName(String groupName) {
        JLabel headerLabel = new JLabel(groupName);
        add(headerLabel, getConstraint(0, lastRow, 1, 1).gridWidth(2));
        lastRow++;
    }

    private void addAxis(Axis axis, String value) {
        params.put(axis, value);

        JLabel label = new JLabel(axis.getHumanReadableName() + ":");
        add(label, getConstraint(0, lastRow, 1, 1));

        JTextField textField = new MyJTextField(value);
        textField.addActionListener(a -> params.put(axis, textField.getText()));
        add(textField, getConstraint(1, lastRow, 1, 1));

        lastRow++;
    }

    private void submitChanges() {
        try {
            Map<Axis, Double> map = params.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> DoubleUtils.nonNegativeParse(e.getValue())));
            map.forEach((k, v) -> model.setInitialValue(k, v));

            Event event = new Event(model, state);
            for (SubmitListener<Event> listener : listeners) {
                listener.onSubmit(event);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Проверьте корректность ввода введенных данных!\n" +
                    "Введенные значения должны быть неотрицательными");
        }
    }

    public void addSubmitListener(SubmitListener<Event> listener) {
        listeners.add(listener);
    }

    public void removeSubmitListener(SubmitListener<Event> listener) {
        listeners.remove(listener);
    }

    private Constraint getConstraint(int gridX, int gridY, int gridWidth, int gridHeight) {
        return Constraint.create(gridX, gridY, gridWidth, gridHeight)
            .fill(GridBagConstraints.HORIZONTAL)
            .weightX(1)
            .insets(3, 5);
    }

    private enum State {
        CREATE,
        EDIT
    }

    public static class Event {
        private final Model model;
        private final State state;

        private Event(Model model, State state) {
            this.model = model;
            this.state = state;
        }

        public Model getModel() {
            return model;
        }

        public State getState() {
            return state;
        }
    }
}

