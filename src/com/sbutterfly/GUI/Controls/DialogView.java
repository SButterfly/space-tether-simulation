package com.sbutterfly.GUI.Controls;

import com.sbutterfly.GUI.Frameable;
import com.sbutterfly.GUI.NavigationController;
import com.sbutterfly.GUI.Panels.Constraint;
import com.sbutterfly.GUI.Panels.JGridBagPanel;
import com.sbutterfly.utils.Log;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Sergei on 08.03.2015.
 */
public class DialogView implements Frameable {

    private JGridBagPanel rootPanel;
    private JButton submitButton;
    private String title;
    private String message;

    private String[] buttonsTitle;
    private JFrame frame;

    public DialogView() {
        title = "Сообщение";
        createGUI();
    }

    public DialogView(String title, String message) {
        buttonsTitle = new String[]{"OK"};
        this.title = title;
        this.message = message;
        createGUI();
    }

    public static void show(String title, String message) {
        NavigationController.open(new DialogView(title, message));
    }

    public static void showError(String message) {
        NavigationController.open(new DialogView("Ошибка", message));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String[] getButtonsTitle() {
        return buttonsTitle.clone();
    }

    private void createGUI() {
        rootPanel = new JGridBagPanel();
        JLabel label = new JLabel();
        label.setText(message);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        submitButton = new JButton(buttonsTitle[0]);
        submitButton.addActionListener(e -> NavigationController.close(this));
        panel.add(submitButton);

        rootPanel.add(label, Constraint.create(0, 0).anchor(GridBagConstraints.CENTER).insets(10).weightY(1).weightX(1));
        rootPanel.add(panel, Constraint.create(0, 1).anchor(GridBagConstraints.EAST).insets(10));

        Log.info(this, "GUI was created");
    }

    @Override
    public JFrame getFrame() {
        if (frame == null) {
            frame = new JFrame(getTitle());
            frame.getContentPane().add(rootPanel);
            frame.pack();
            frame.setSize(300, 200);
            frame.getRootPane().setDefaultButton(submitButton);
        }
        return frame;
    }
}
