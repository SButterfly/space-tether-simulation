package com.sbutterfly.GUI.controls;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Sergei on 28.02.2015.
 */
public class EmptyPanel extends JPanel {
    public EmptyPanel() {
    }

    public EmptyPanel(int width, int height) {
        this();
        setMinimumSize(new Dimension(width, height));
        setPreferredSize(new Dimension(width, height));
    }
}
