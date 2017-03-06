package com.sbutterfly.gui.controls;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;

/**
 * Created by Sergei on 15.02.2015.
 */
@SuppressWarnings("magicnumber")
public class MyJTextField extends JTextField {

    public MyJTextField() {
        init();
    }

    public MyJTextField(String text) {
        super(text);
        init();
    }

    public MyJTextField(int columns) {
        super(columns);
        init();
    }

    public MyJTextField(String text, int columns) {
        super(text, columns);
        init();
    }

    public MyJTextField(Document doc, String text, int columns) {
        super(doc, text, columns);
        init();
    }

    private void init(){
        setMinimumSize(new Dimension(50, 20));
        setPreferredSize(getMinimumSize());
    }
}
