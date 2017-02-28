package com.sbutterfly.gui.hardcoded;

import com.sbutterfly.gui.InitialStateView;
import com.sbutterfly.gui.helpers.EventListener;
import com.sbutterfly.gui.controls.MyJTextField;
import com.sbutterfly.gui.panels.Constraint;
import com.sbutterfly.core.BaseSystem;
import com.sbutterfly.core.rope.RopeSystem;
import com.sbutterfly.differential.Index;
import com.sbutterfly.utils.DoubleUtils;
import com.sbutterfly.utils.Log;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Sergei on 08.03.2015.
 */
public class RopeInitialStateView extends InitialStateView {
    int lastRow = 0;
    private MyJTextField m1TextField;
    private MyJTextField m2TextField;
    private MyJTextField pTextField;

    private MyJTextField aTextField;
    private MyJTextField bTextField;
    private MyJTextField lkTextField;
    private MyJTextField hTextField;

    private MyJTextField s0TextField;
    private MyJTextField s1TextField;
    private MyJTextField s2TextField;
    private MyJTextField s3TextField;
    private MyJTextField s4TextField;
    private MyJTextField s5TextField;

    private JButton submitButton;

    private boolean continueSubmitHack = true;

    public RopeInitialStateView(BaseSystem model) {
        super(model);
        if (!(model instanceof RopeSystem)) {
            throw new IllegalArgumentException("model must be type of RopeModel");
        }
        if (model.paramsNames().length != 6 && model.initialParamsNames().length != 6) {
            throw new IllegalArgumentException("model prams lengths aren't equal");
        }
    }

    @Override
    protected void createGUI() {
        submitButton = new JButton("Рассчитать");
        submitButton.addActionListener(e -> {
            if (continueSubmitHack) {
                for (EventListener<BaseSystem> listener : listeners) {
                    listener.onSubmit(getSystem());
                }
            } else {
                continueSubmitHack = true;
            }
        });

        add(new JLabel("Параметры тросовой системы:"), Constraint.create(0, lastRow++, 2, 1).insets(5).fill(GridBagConstraints.HORIZONTAL));

        add(new JLabel(getSystem().initialParamsNames()[0] + ":", SwingConstants.RIGHT), Constraint.create(0, lastRow).fill(GridBagConstraints.HORIZONTAL));
        add((m1TextField = new MyJTextField(DoubleUtils.toString(getSystem().getInitialParameter(0)))), Constraint.create(1, lastRow++));
        submitButton.addActionListener(e -> onSet(m1TextField, value -> getSystem().setInitialParameter(0, value)));

        add(new JLabel(getSystem().initialParamsNames()[1] + ":", SwingConstants.RIGHT), Constraint.create(0, lastRow).fill(GridBagConstraints.HORIZONTAL));
        add((m2TextField = new MyJTextField(DoubleUtils.toString(getSystem().getInitialParameter(1)))), Constraint.create(1, lastRow++));
        submitButton.addActionListener(e -> onSet(m2TextField, value -> getSystem().setInitialParameter(1, value)));

        add(new JLabel(getSystem().initialParamsNames()[2] + ":", SwingConstants.RIGHT), Constraint.create(0, lastRow).fill(GridBagConstraints.HORIZONTAL));
        add((pTextField = new MyJTextField(DoubleUtils.toString(getSystem().getInitialParameter(2)))), Constraint.create(1, lastRow++));
        submitButton.addActionListener(e -> onSet(pTextField, value -> getSystem().setInitialParameter(2, value)));

        add(new JLabel(getSystem().initialParamsNames()[5] + ":", SwingConstants.RIGHT), Constraint.create(0, lastRow).fill(GridBagConstraints.HORIZONTAL));
        add((lkTextField = new MyJTextField(DoubleUtils.toString(getSystem().getInitialParameter(5)))), Constraint.create(1, lastRow++));
        submitButton.addActionListener(e -> onSet(lkTextField, value -> getSystem().setInitialParameter(5, value)));

        add(new JLabel(getSystem().initialParamsNames()[6] + ":", SwingConstants.RIGHT), Constraint.create(0, lastRow).fill(GridBagConstraints.HORIZONTAL));
        add((hTextField = new MyJTextField(DoubleUtils.toString(getSystem().getInitialParameter(6)))), Constraint.create(1, lastRow++));
        submitButton.addActionListener(e -> onSet(hTextField, value -> getSystem().setInitialParameter(6, value)));

        add(new JLabel("Параметры закона:"), Constraint.create(0, lastRow++, 2, 1).insets(5).fill(GridBagConstraints.HORIZONTAL));

        add(new JLabel(getSystem().initialParamsNames()[3] + ":", SwingConstants.RIGHT), Constraint.create(0, lastRow).fill(GridBagConstraints.HORIZONTAL));
        add((aTextField = new MyJTextField(DoubleUtils.toString(getSystem().getInitialParameter(3)))), Constraint.create(1, lastRow++));
        submitButton.addActionListener(e -> onSet(aTextField, value -> getSystem().setInitialParameter(3, value)));

        add(new JLabel(getSystem().initialParamsNames()[4] + ":", SwingConstants.RIGHT), Constraint.create(0, lastRow).fill(GridBagConstraints.HORIZONTAL));
        add((bTextField = new MyJTextField(DoubleUtils.toString(getSystem().getInitialParameter(4)))), Constraint.create(1, lastRow++));
        submitButton.addActionListener(e -> onSet(bTextField, value -> getSystem().setInitialParameter(4, value)));

        add(new JLabel("Начальные параметры:"), Constraint.create(0, lastRow++, 2, 1).insets(5).fill(GridBagConstraints.HORIZONTAL));

        add(new JLabel(getSystem().getFullAxisName(new Index(0)) + ":", SwingConstants.RIGHT), Constraint.create(0, lastRow).fill(GridBagConstraints.HORIZONTAL));
        add((s0TextField = new MyJTextField(DoubleUtils.toString(getSystem().getStartParameter(0)))), Constraint.create(1, lastRow++));
        submitButton.addActionListener(e -> onSet(s0TextField, value -> getSystem().setStartParameter(0, value)));

        add(new JLabel(getSystem().getFullAxisName(new Index(1)) + ":", SwingConstants.RIGHT), Constraint.create(0, lastRow).fill(GridBagConstraints.HORIZONTAL));
        add((s1TextField = new MyJTextField(DoubleUtils.toString(getSystem().getStartParameter(1)))), Constraint.create(1, lastRow++));
        submitButton.addActionListener(e -> onSet(s1TextField, value -> getSystem().setStartParameter(1, value)));

        add(new JLabel(getSystem().getFullAxisName(new Index(2)) + ":", SwingConstants.RIGHT), Constraint.create(0, lastRow).fill(GridBagConstraints.HORIZONTAL));
        add((s2TextField = new MyJTextField(DoubleUtils.toString(getSystem().getStartParameter(2)))), Constraint.create(1, lastRow++));
        submitButton.addActionListener(e -> onSet(s2TextField, value -> getSystem().setStartParameter(2, value)));

        add(new JLabel(getSystem().getFullAxisName(new Index(3)) + ":", SwingConstants.RIGHT), Constraint.create(0, lastRow).fill(GridBagConstraints.HORIZONTAL));
        add((s3TextField = new MyJTextField(DoubleUtils.toString(getSystem().getStartParameter(3)))), Constraint.create(1, lastRow++));
        submitButton.addActionListener(e -> onSet(s3TextField, value -> getSystem().setStartParameter(3, value)));

        add(new JLabel(getSystem().getFullAxisName(new Index(4)) + ":", SwingConstants.RIGHT), Constraint.create(0, lastRow).fill(GridBagConstraints.HORIZONTAL));
        add((s4TextField = new MyJTextField(DoubleUtils.toString(getSystem().getStartParameter(4)))), Constraint.create(1, lastRow++));
        submitButton.addActionListener(e -> onSet(s4TextField, value -> getSystem().setStartParameter(4, value)));

        add(new JLabel(getSystem().getFullAxisName(new Index(5)) + ":", SwingConstants.RIGHT), Constraint.create(0, lastRow).fill(GridBagConstraints.HORIZONTAL));
        add((s5TextField = new MyJTextField(DoubleUtils.toString(getSystem().getStartParameter(5)))), Constraint.create(1, lastRow++));
        submitButton.addActionListener(e -> onSet(s5TextField, value -> getSystem().setStartParameter(5, value)));

        add(submitButton, Constraint.create(0, lastRow, 2, 1).anchor(GridBagConstraints.EAST).insets(5));

        Log.debug(this, "GUI was created");
    }

    @Override
    public RopeSystem getSystem() {
        return (RopeSystem) super.getSystem();
    }
}
