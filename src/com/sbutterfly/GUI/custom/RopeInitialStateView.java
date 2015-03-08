package com.sbutterfly.GUI.custom;

import com.sbutterfly.GUI.InitialStateView;
import com.sbutterfly.GUI.SubmitListener;
import com.sbutterfly.GUI.controls.MyJTextField;
import com.sbutterfly.GUI.panels.Constraint;
import com.sbutterfly.core.ODEBaseModel;
import com.sbutterfly.core.rope.RopeModel;
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
    private MyJTextField LkTextField;

    private MyJTextField s0TextField;
    private MyJTextField s1TextField;
    private MyJTextField s2TextField;
    private MyJTextField s3TextField;
    private MyJTextField s4TextField;
    private MyJTextField s5TextField;

    private JButton submitButton;

    public RopeInitialStateView(ODEBaseModel model) {
        super(model);
        if (!(model instanceof RopeModel)) {
            throw new IllegalArgumentException("model must be type of RopeModel");
        }
        if (model.paramsNames().length != 6 && model.initialParamsNames().length != 6) {
            throw new IllegalArgumentException("model prams legthes aren't equalz");
        }
    }

    @Override
    protected void createGUI() {
        add(new JLabel("Параметры троссовой системы:"), Constraint.create(0, lastRow++, 2, 1).insets(5).fill(GridBagConstraints.HORIZONTAL));

        add(new JLabel(getModel().initialParamsNames()[0] + ":", SwingConstants.RIGHT), Constraint.create(0, lastRow).fill(GridBagConstraints.HORIZONTAL));
        add((m1TextField = new MyJTextField(DoubleUtils.toString(getModel().getInitialParameter(0)))), Constraint.create(1, lastRow++));

        add(new JLabel(getModel().initialParamsNames()[1] + ":", SwingConstants.RIGHT), Constraint.create(0, lastRow).fill(GridBagConstraints.HORIZONTAL));
        add((m2TextField = new MyJTextField(DoubleUtils.toString(getModel().getInitialParameter(1)))), Constraint.create(1, lastRow++));

        add(new JLabel(getModel().initialParamsNames()[2] + ":", SwingConstants.RIGHT), Constraint.create(0, lastRow).fill(GridBagConstraints.HORIZONTAL));
        add((pTextField = new MyJTextField(DoubleUtils.toString(getModel().getInitialParameter(2)))), Constraint.create(1, lastRow++));


        add(new JLabel("Параметры закона:"), Constraint.create(0, lastRow++, 2, 1).insets(5).fill(GridBagConstraints.HORIZONTAL));

        add(new JLabel(getModel().initialParamsNames()[3] + ":", SwingConstants.RIGHT), Constraint.create(0, lastRow).fill(GridBagConstraints.HORIZONTAL));
        add((aTextField = new MyJTextField(DoubleUtils.toString(getModel().getInitialParameter(3)))), Constraint.create(1, lastRow++));

        add(new JLabel(getModel().initialParamsNames()[4] + ":", SwingConstants.RIGHT), Constraint.create(0, lastRow).fill(GridBagConstraints.HORIZONTAL));
        add((bTextField = new MyJTextField(DoubleUtils.toString(getModel().getInitialParameter(4)))), Constraint.create(1, lastRow++));

        add(new JLabel(getModel().initialParamsNames()[5] + ":", SwingConstants.RIGHT), Constraint.create(0, lastRow).fill(GridBagConstraints.HORIZONTAL));
        add((LkTextField = new MyJTextField(DoubleUtils.toString(getModel().getInitialParameter(5)))), Constraint.create(1, lastRow++));


        add(new JLabel("Начальные параметры:"), Constraint.create(0, lastRow++, 2, 1).insets(5).fill(GridBagConstraints.HORIZONTAL));

        add(new JLabel(getModel().getFullAxisName(new Index(0)) + ":", SwingConstants.RIGHT), Constraint.create(0, lastRow).fill(GridBagConstraints.HORIZONTAL));
        add((s0TextField = new MyJTextField(DoubleUtils.toString(getModel().getStartParameter(0)))), Constraint.create(1, lastRow++));

        add(new JLabel(getModel().getFullAxisName(new Index(1)) + ":", SwingConstants.RIGHT), Constraint.create(0, lastRow).fill(GridBagConstraints.HORIZONTAL));
        add((s1TextField = new MyJTextField(DoubleUtils.toString(getModel().getStartParameter(1)))), Constraint.create(1, lastRow++));

        add(new JLabel(getModel().getFullAxisName(new Index(2)) + ":", SwingConstants.RIGHT), Constraint.create(0, lastRow).fill(GridBagConstraints.HORIZONTAL));
        add((s2TextField = new MyJTextField(DoubleUtils.toString(getModel().getStartParameter(2)))), Constraint.create(1, lastRow++));

        add(new JLabel(getModel().getFullAxisName(new Index(3)) + ":", SwingConstants.RIGHT), Constraint.create(0, lastRow).fill(GridBagConstraints.HORIZONTAL));
        add((s3TextField = new MyJTextField(DoubleUtils.toString(getModel().getStartParameter(3)))), Constraint.create(1, lastRow++));

        add(new JLabel(getModel().getFullAxisName(new Index(4)) + ":", SwingConstants.RIGHT), Constraint.create(0, lastRow).fill(GridBagConstraints.HORIZONTAL));
        add((s4TextField = new MyJTextField(DoubleUtils.toString(getModel().getStartParameter(4)))), Constraint.create(1, lastRow++));

        add(new JLabel(getModel().getFullAxisName(new Index(5)) + ":", SwingConstants.RIGHT), Constraint.create(0, lastRow).fill(GridBagConstraints.HORIZONTAL));
        add((s5TextField = new MyJTextField(DoubleUtils.toString(getModel().getStartParameter(5)))), Constraint.create(1, lastRow++));

        submitButton = new JButton("Расчитать");
        submitButton.addActionListener(e -> {
            for (SubmitListener<ODEBaseModel> listener : list) {
                listener.onSubmit(getModel());
            }
        });

        add(submitButton, Constraint.create(0, lastRow, 2, 1).anchor(GridBagConstraints.EAST).insets(5));

        Log.debug(this, "GUI was created");
    }

    @Override
    public RopeModel getModel() {
        return (RopeModel) super.getModel();
    }
}
