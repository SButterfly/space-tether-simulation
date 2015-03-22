package com.sbutterfly.GUI.controls;

import javax.swing.*;

/**
 * Created by Sergei on 09.03.2015.
 */
public class MultiLineJLabel extends JLabel {

    public MultiLineJLabel(String text, Icon icon, int horizontalAlignment) {
        super(text, icon, horizontalAlignment);
    }

    public MultiLineJLabel(String text, int horizontalAlignment) {
        super(text, horizontalAlignment);
    }

    public MultiLineJLabel(String text) {
        super(text);
    }

    public MultiLineJLabel(Icon image, int horizontalAlignment) {
        super(image, horizontalAlignment);
    }

    public MultiLineJLabel(Icon image) {
        super(image);
    }

    public MultiLineJLabel() {
    }

    @Override
    public void setText(String text) {
        super.setText("<html>" + text.replace("\n", "<br>") + "</html>");
    }
}
