package com.sbutterfly.GUI.Controls;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Sergei on 21.02.2015.
 */
public class JImageButton extends JButton {
    public JImageButton() {
        init();
    }

    public JImageButton(Icon icon) {
        super(icon);
        init();
    }

    public JImageButton(String imagePath) {
        init();
        ImageIcon icon = new ImageIcon(imagePath);
        int width = 17 * icon.getIconWidth() / icon.getIconHeight();
        Image image = icon.getImage().getScaledInstance(width, 17, Image.SCALE_SMOOTH);
        setIcon(new ImageIcon(image));
    }

    public JImageButton(Action a) {
        super(a);
        init();
    }

    private void init() {
        setBorder(BorderFactory.createEmptyBorder());
        setContentAreaFilled(false);
        setMaximumSize(new Dimension(20, 20));
        setPreferredSize(getMaximumSize());
    }
}
