package com.sbutterfly.gui;

import com.sbutterfly.gui.panels.Constraint;
import com.sbutterfly.gui.panels.JGridBagPanel;
import com.sbutterfly.utils.Log;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.GridBagConstraints;

/**
 * Created by Sergei on 31.01.2015.
 */
@SuppressWarnings("magicnumber")
public class AdditionalLineView extends JGridBagPanel {

    private JLabel label;
    private JProgressBar progressBar;

    public AdditionalLineView() {
        createGUI();
    }

    private void createGUI() {
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));

        label = new JLabel();

        progressBar = new JProgressBar();
        progressBar.setMaximum(100);
        progressBar.setMinimum(0);
        progressBar.setPreferredSize(new Dimension(200, 20));
        progressBar.setMaximumSize(new Dimension(200, 20));

        add(label, Constraint.create(0, 0).weightX(1).fill(GridBagConstraints.HORIZONTAL)
                .anchor(GridBagConstraints.WEST).insets(3, 5));
        add(progressBar, Constraint.create(1, 0).insets(3, 5));

        setStatusIndicator(0);

        Log.debug(this, "GUI was created");
    }

    public String getText() {
        return label.getText();
    }

    public void setText(String value) {
        label.setText(value);
    }

    public void setStatusIndicator(double value) {
        SwingUtilities.invokeLater(() -> {
            int val = (int) Math.round(value * 100);
            progressBar.setValue(val);
            progressBar.setStringPainted(val != 0);
        });
    }
}
