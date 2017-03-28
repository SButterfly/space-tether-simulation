package com.sbutterfly.gui.controls;

import com.sbutterfly.gui.helpers.EventListener;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.awt.Dimension;

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

    private void init() {
        setMinimumSize(new Dimension(50, 20));
        setPreferredSize(getMinimumSize());
    }

    public void setWidth(int width) {
        setSize(new Dimension(width, getSize().height));
    }

    public void setHeight(int height) {
        setSize(new Dimension(getSize().width, height));
    }

    public void setMinimalWidth(int minimalWidth) {
        setMinimumSize(new Dimension(minimalWidth, getMinimumSize().height));
    }

    public void setMinimalHeight(int minimalHeight) {
        setMinimumSize(new Dimension(getMinimumSize().width, minimalHeight));
    }

    // TODO quick fix
    public void addTextChangedListener(EventListener<String> textChangedListener) {
        getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                textChangedListener.onSubmit(getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                textChangedListener.onSubmit(getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                textChangedListener.onSubmit(getText());
            }
        });
    }
}
