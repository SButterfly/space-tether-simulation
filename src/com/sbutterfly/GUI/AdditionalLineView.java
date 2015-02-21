package com.sbutterfly.GUI;

import com.sbutterfly.helpers.Log;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Sergei on 31.01.2015.
 */
@Deprecated
public class AdditionalLineView extends JPanel {

    private JLabel label;
    private JProgressBar progressBar;
    private JButton cancelButton;

    public AdditionalLineView() {
        createGUI();
    }

    private void createGUI() {

        setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        setLayout(new GridLayout(1, 2));
        setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));

        label = new JLabel();

        progressBar = new JProgressBar();
        progressBar.setMaximum(100);
        progressBar.setMinimum(0);
        progressBar.setStringPainted(true);
        progressBar.setMaximumSize(new Dimension(100, 20));

        add(label);
        add(progressBar);

        try {
            BufferedImage buttonIcon = ImageIO.read(new File("assets/cancel.png"));
            cancelButton = new JButton(new ImageIcon(buttonIcon));
            cancelButton.setBorder(BorderFactory.createEmptyBorder());
            cancelButton.setContentAreaFilled(false);
            cancelButton.setMaximumSize(new Dimension(20,20));
            cancelButton.setSize(20,20);

            add(cancelButton);

        } catch (IOException e) {
            Log.error(this, e.toString());
        }

        setStatusIndicator(0);

        Log.debug(this, "GUI was created");
    }

    public String getText(){
        return label.getText();
    }

    public void setText(String value) {
        label.setText(value);
    }

    public double getStatusIndicator(){
        return progressBar.getPercentComplete();
    }

    public void setStatusIndicator(double value){
        int val = (int) Math.round(value*100);
        progressBar.setValue(val);
        progressBar.setVisible(val != 0);
        cancelButton.setVisible(val != 0);
    }

    public void addCancelActionListener(ActionListener listener){
        cancelButton.addActionListener(listener);
    }
    public void removeCancelActionListener(ActionListener listener){
        cancelButton.removeActionListener(listener);
    }
}
