package com.sbutterfly.GUI;

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

    private static Stack<Frameable> navigationStack = new Stack<>();

    public static void open(Frameable view) {

        JFrame frame = view.getFrame();
        int closeOperation = JFrame.EXIT_ON_CLOSE;

        if (!navigationStack.empty()){
            closeOperation = JFrame.DO_NOTHING_ON_CLOSE;
            navigationStack.peek().getFrame().setEnabled(false);
        }

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                close(view);
            }
        });
        frame.setDefaultCloseOperation(closeOperation);
        frame.setVisible(true);
        navigationStack.push(view);

        Log.debug(LTAG, "open " + view.getClass().getName());
    }

    public static void close() {
        close(navigationStack.peek());
    }

    public static void close(Frameable view) {
        JFrame frame = view.getFrame();
        frame.setVisible(false);
        frame.dispose();
        navigationStack.remove(view);

        if (!navigationStack.empty()) {
            JFrame topFrame = navigationStack.peek().getFrame();
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
