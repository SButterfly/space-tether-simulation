package com.sbutterfly.gui.controls;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;

/**
 * Created by Sergei on 15.02.2015.
 */
public class MyJTextField extends JTextField {

    public MyJTextField() {
        Init();
    }

    public MyJTextField(String text) {
        super(text);
        Init();
    }

    public MyJTextField(int columns) {
        super(columns);
        Init();
    }

    public MyJTextField(String text, int columns) {
        super(text, columns);
        Init();
    }

    public MyJTextField(Document doc, String text, int columns) {
        super(doc, text, columns);
        Init();
    }

    private void Init(){
        setMinimumSize(new Dimension(50, 20));
        setPreferredSize(getMinimumSize());
    }
}
