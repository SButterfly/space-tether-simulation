package com.sbutterfly.gui;

import com.sbutterfly.engine.GroupAxisDescription;
import com.sbutterfly.engine.Model;
import com.sbutterfly.engine.trace.Axis;
import com.sbutterfly.gui.controls.EmptyPanel;
import com.sbutterfly.gui.controls.MyJTextField;
import com.sbutterfly.gui.helpers.EventHandler;
import com.sbutterfly.gui.helpers.EventListener;
import com.sbutterfly.gui.panels.Constraint;
import com.sbutterfly.gui.panels.JGridBagPanel;
import com.sbutterfly.utils.DoubleUtils;
import com.sbutterfly.utils.Log;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Sergei on 02.02.2015.
 */
@SuppressWarnings("magicnumber")
public class InitialStateView extends JGridBagPanel {

    private final EventHandler<Event> eventHandler = new EventHandler<>();

    // Используется String вместо Double, чтобы валидация происходила в самом конце перед сохранением.
    private final Map<Axis, String> params = new HashMap<>();

    private Model model;
    private boolean edit;

    private int lastRow = 0;

    public InitialStateView() {
    }

    public Model getModel() {
        return model;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setModel(Model model, boolean edit) {
        this.model = model;
        this.edit = edit;
        clear();
        createGUI();
    }

    private void createGUI() {
        List<GroupAxisDescription> list = model.getModelDescription();
        for (GroupAxisDescription groupAxisDescription : list) {
            addGroupName(groupAxisDescription.getName());
            for (int i = 0; i < groupAxisDescription.size(); i += 2) {
                Axis left = groupAxisDescription.get(i);
                Axis right = i + 1 < groupAxisDescription.size()
                        ? groupAxisDescription.get(i + 1)
                        : null;
                if (left != null) {
                    addAxis(left, DoubleUtils.toString(model.getInitialValue(left)), false);
                }
                if (right != null) {
                    addAxis(right, DoubleUtils.toString(model.getInitialValue(right)), true);
                }
                lastRow++;
            }
        }

        add(new EmptyPanel(), getConstraint(0, lastRow++, 1, 1).gridWidth(4));

        JGridBagPanel jGridBagPanel = new JGridBagPanel();
        JLabel nameLabel = new JLabel("Имя:");
        MyJTextField textField = new MyJTextField();
        textField.setText(model.getName());
        textField.setPreferredSize(new Dimension(150, textField.getPreferredSize().height));
        jGridBagPanel.add(nameLabel, Constraint.create(0, 0));
        jGridBagPanel.add(textField, Constraint.create(1, 0).weightX(1).weightY(1));

        if (edit) {
            JButton saveButton = new JButton("Сохранить");
            JButton cancelButton = new JButton("Отменить");
            saveButton.addActionListener(l -> {
                String name = textField.getText();
                submitChanges(name, State.EDIT);
            });
            cancelButton.addActionListener(l -> {
                Event event = new Event(model, State.CANCEL);
                eventHandler.invoke(event);
            });
            jGridBagPanel.add(saveButton, Constraint.create(2, 0));
            jGridBagPanel.add(cancelButton, Constraint.create(3, 0));
        } else {
            JButton submitButton = new JButton();
            submitButton.setText("Добавить");
            submitButton.addActionListener(l -> {
                String name = textField.getText();
                submitChanges(name, State.CREATE);
            });
            jGridBagPanel.add(submitButton, Constraint.create(3, 0));
        }
        add(jGridBagPanel, getConstraint(0, lastRow, 4, 1)
                .fill(GridBagConstraints.HORIZONTAL)
                .anchor(GridBagConstraints.EAST));

        Log.debug(this, "GUI updated");
    }

    private void clear() {
        removeAll();
        params.clear();
    }

    private void addGroupName(String groupName) {
        JLabel headerLabel = new JLabel(groupName);
        add(headerLabel, getConstraint(0, lastRow, 1, 1).gridWidth(4));
        lastRow++;
    }

    private void addAxis(Axis axis, String value, boolean right) {
        params.put(axis, value);

        int column = right ? 2 : 0;

        JLabel label = new JLabel(axis.getHumanReadableName() + ":");
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        add(label, getConstraint(column, lastRow, 1, 1).anchor(GridBagConstraints.EAST));

        MyJTextField textField = new MyJTextField(value);
        textField.addTextChangedListener(a -> params.put(axis, textField.getText()));
        add(textField, getConstraint(column + 1, lastRow, 1, 1));
    }

    private void submitChanges(String modelName, State state) {
        try {
            Map<Axis, Double> map = params.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> DoubleUtils.nonNegativeParse(e.getValue())));
            map.forEach((k, v) -> model.setInitialValue(k, v));
            model.setName(modelName);

            Event event = new Event(model, state);
            eventHandler.invoke(event);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Проверьте корректность ввода введенных данных!\n" +
                    "Введенные значения должны быть неотрицательными");
        }
    }

    public void addSubmitListener(EventListener<Event> listener) {
        eventHandler.add(listener);
    }

    public void removeSubmitListener(EventListener<Event> listener) {
        eventHandler.remove(listener);
    }

    private Constraint getConstraint(int gridX, int gridY, int gridWidth, int gridHeight) {
        return Constraint.create(gridX, gridY, gridWidth, gridHeight)
            .fill(GridBagConstraints.HORIZONTAL)
            .weightX(1)
            .insets(3, 5);
    }

    public enum State {
        CREATE,
        EDIT,
        CANCEL
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

