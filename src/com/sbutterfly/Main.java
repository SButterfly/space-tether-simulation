package com.sbutterfly;

import com.sbutterfly.GUI.*;
import com.sbutterfly.differential.*;
import com.sbutterfly.pendulum.PendulumFunction;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        setSystemViewTheme();
        NavigationController.Open(new MainView());
    }

    private static void setSystemViewTheme(){
        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException e) {
            // handle exception
        }
        catch (ClassNotFoundException e) {
            // handle exception
        }
        catch (InstantiationException e) {
            // handle exception
        }
        catch (IllegalAccessException e) {
            // handle exception
        }
    }
}
