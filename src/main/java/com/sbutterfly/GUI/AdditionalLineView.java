package com.sbutterfly.GUI;

import com.sbutterfly.GUI.controls.JImageButton;
import com.sbutterfly.GUI.panels.Constraint;
import com.sbutterfly.GUI.panels.JGridBagPanel;
import com.sbutterfly.utils.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;

/**
 * Created by Sergei on 31.01.2015.
 */
public class AdditionalLineView extends JGridBagPanel {

    private final static URL CANCEL_IMAGE = AdditionalLineView.class
            .getClassLoader().getResource("cancel.png");

    Processable processable;
    private JLabel label;
    private JProgressBar progressBar;
    private JButton cancelButton;

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

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.add(progressBar);

        cancelButton = new JImageButton(CANCEL_IMAGE);
        cancelButton.addActionListener(e -> {
            if (processable != null) {
                processable.cancel();
            }
        });
        panel.add(cancelButton);

        add(label, Constraint.create(0, 0).weightX(1).fill(GridBagConstraints.HORIZONTAL).anchor(GridBagConstraints.WEST).insets(3, 5));
        add(panel, Constraint.create(1, 0).insets(3, 5));

        setStatusIndicator(0);

        Log.debug(this, "GUI was created");
    }

    public void setProcessable(Processable processable) {
        this.processable = processable;
        new Thread(() -> {
            while (!processable.hasEnded()) {
                setStatusIndicator(processable.getStatusIndicator());
                try {
                    Thread.sleep((long) 0.1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            setStatusIndicator(0);
            this.processable = null;
        }).start();
    }

    public String getText(){
        return label.getText();
    }

    public void setText(String value) {
        SwingUtilities.invokeLater(() -> label.setText(value));
    }

    public double getStatusIndicator(){
        return progressBar.getPercentComplete();
    }

    public void setStatusIndicator(double value){
        SwingUtilities.invokeLater(() -> {
            int val = (int) Math.round(value * 100);
            progressBar.setValue(val);
            progressBar.setStringPainted(val != 0);
            cancelButton.setEnabled(val != 0);
        });
    }

    public void addCancelActionListener(ActionListener listener){
        cancelButton.addActionListener(listener);
    }
    public void removeCancelActionListener(ActionListener listener){
        cancelButton.removeActionListener(listener);
    }

    public static interface Processable {
        double getStatusIndicator();

        boolean hasEnded();

        boolean hasCanceled();

        void cancel();
    }
}
