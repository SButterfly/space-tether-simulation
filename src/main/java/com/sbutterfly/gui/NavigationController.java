package com.sbutterfly.gui;

import com.sbutterfly.utils.Log;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Stack;

/**
 * Created by Sergei on 01.02.2015.
 */
public class NavigationController {

    private static final String LTAG = NavigationController.class.getSimpleName();

    private static final Stack<Frameable> NAVIGATION_STACK = new Stack<>();

    private NavigationController() {
    }

    public static void open(Frameable view) {
        JFrame frame = view.getFrame();
        int closeOperation = JFrame.EXIT_ON_CLOSE;

        if (!NAVIGATION_STACK.empty()) {
            closeOperation = JFrame.DO_NOTHING_ON_CLOSE;
            NAVIGATION_STACK.peek().getFrame().setEnabled(false);
        }

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                close(view);
            }
        });
        frame.setDefaultCloseOperation(closeOperation);
        frame.setVisible(true);
        NAVIGATION_STACK.push(view);

        Log.debug(LTAG, "open " + view.getClass().getName());
    }

    public static void close() {
        close(NAVIGATION_STACK.peek());
    }

    public static void close(Frameable view) {
        JFrame frame = view.getFrame();
        frame.setVisible(false);
        frame.dispose();
        NAVIGATION_STACK.remove(view);

        if (!NAVIGATION_STACK.empty()) {
            JFrame topFrame = NAVIGATION_STACK.peek().getFrame();
            topFrame.setEnabled(true);
            topFrame.setAlwaysOnTop(true);
            topFrame.setAlwaysOnTop(false);
        }

        Log.debug(LTAG, "close " + view.getClass().getName());
    }

    public static JFrame getEmptyFrame() {
        JFrame frame = new JFrame();
        frame.setSize(500, 500);
        return frame;
    }
}
