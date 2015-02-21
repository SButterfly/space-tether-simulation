package com.sbutterfly.GUI.Panels;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Sergei on 21.02.2015.
 */
public class JBoxLayout extends JPanel {
    public JBoxLayout(int axis, boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        init(axis);
    }

    public JBoxLayout(int axis) {
        init(axis);
    }

    public void init(int axis) {
        setLayout(new BoxLayout(this, axis));
    }

    @Override
    public void setLayout(LayoutManager mgr) {
        //HACK: check for FlowLayout needs to super initialization
        if (!(mgr instanceof BoxLayout) && !(mgr instanceof FlowLayout)) {
            throw new IllegalArgumentException();
        }
        super.setLayout(mgr);
    }
}
