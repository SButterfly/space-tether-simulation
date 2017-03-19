package com.sbutterfly.gui.panels;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;

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

    public void add(Component comp, Constraint constraint, int index) {
        super.add(comp, constraint.get(), index);
    }
}
