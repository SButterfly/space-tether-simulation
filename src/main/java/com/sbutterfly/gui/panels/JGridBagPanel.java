package com.sbutterfly.gui.panels;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Sergei on 21.02.2015.
 */
public class JGridBagPanel extends JPanel {

    public JGridBagPanel(boolean isDoubleBuffered) {
        super(new GridBagLayout(), isDoubleBuffered);
    }

    public JGridBagPanel() {
        super(new GridBagLayout());
    }

    @Override
    public void setLayout(LayoutManager mgr) {
        if (!(mgr instanceof GridBagLayout)) {
            throw new IllegalArgumentException();
        }
        super.setLayout(mgr);
    }

    public void add(JComponent component, Constraint constraint) {
        super.add(component, constraint.get());
    }
}
