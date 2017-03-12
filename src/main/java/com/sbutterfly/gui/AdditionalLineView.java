package com.sbutterfly.gui;

import com.sbutterfly.gui.controls.EmptyPanel;
import com.sbutterfly.gui.panels.Constraint;
import com.sbutterfly.gui.panels.JGridBagPanel;
import com.sbutterfly.utils.Log;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Created by Sergei on 31.01.2015.
 */
@SuppressWarnings("magicnumber")
public class AdditionalLineView extends JGridBagPanel {

    private JPanel loadingPanel;

    public AdditionalLineView() {
        createGUI();
        idle();
    }

    private void createGUI() {
        loadingPanel = loadingPanel();
        add(loadingPanel, Constraint.create(0, 0));
        Log.debug(this, "GUI was created");
    }

    public void busy() {
        loadingPanel.setVisible(true);
    }

    public void idle() {
        loadingPanel.setVisible(false);
    }

    private JPanel loadingPanel() {
        JPanel panel = new JPanel();
        BoxLayout layoutMgr = new BoxLayout(panel, BoxLayout.LINE_AXIS);
        panel.setLayout(layoutMgr);

        ClassLoader cldr = this.getClass().getClassLoader();
        java.net.URL imageURL = cldr.getResource("spinner.gif");
        ImageIcon imageIcon = new ImageIcon(imageURL);
        JLabel iconLabel = new JLabel();
        iconLabel.setIcon(imageIcon);
        imageIcon.setImageObserver(iconLabel);

        JLabel label = new JLabel("Загрузка...");
        panel.add(iconLabel);
        panel.add(new EmptyPanel(5, 5));
        panel.add(label);
        return panel;
    }
}
