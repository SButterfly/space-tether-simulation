package com.sbutterfly.gui_name.controls;

import com.sbutterfly.gui_name.helpers.EventListener;

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
