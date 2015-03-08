package com.sbutterfly.services;

import com.sbutterfly.differential.EulerODEMethod;
import com.sbutterfly.differential.ODEMethod;
import com.sbutterfly.differential.RungeKuttaODEMethod;

import java.util.prefs.Preferences;

/**
 * Created by Sergei on 01.02.2015.
 */
public class AppSettings {

    private static final String ODE_METHOD_KEY = "odemethod";
    private static final String ODE_TIME_KEY = "odetime";
    private static final String ODE_STEP_KEY = "odestep";

    private static Preferences getPreferences(){
        return Preferences.userNodeForPackage(AppSettings.class);
    }

    public static ODEMethod getODEMethod(){
        Preferences preferences = getPreferences();

        int methodInt = preferences.getInt(ODE_METHOD_KEY, 0);

        if (methodInt == 0){
            return new RungeKuttaODEMethod();
        }
        if (methodInt == 1){
            return new EulerODEMethod();
        }

        throw new RuntimeException("Couldn't get ODEMethod");
    }

    public static void setODEMethod(ODEMethod method){
        Preferences preferences = getPreferences();
        int val = -1;
        if (method instanceof RungeKuttaODEMethod){
            val = 0;
        }
        if (method instanceof EulerODEMethod){
            val = 1;
        }

        if (val != -1) {
            preferences.putInt(ODE_METHOD_KEY, val);
            return;
        }

        throw new RuntimeException("Couldn't save this type of ODEMethod");
    }

    public static double getODETime(){
        Preferences preferences = getPreferences();
        return preferences.getDouble(ODE_TIME_KEY, 1000);
    }

    public static void setODETime(double time){
        Preferences preferences = getPreferences();
        preferences.putDouble(ODE_TIME_KEY, time);
    }

    public static double getODEStep() {
        Preferences preferences = getPreferences();
        return preferences.getDouble(ODE_STEP_KEY, 0.01);
    }

    public static void setODEStep(double h) {
        Preferences preferences = getPreferences();
        preferences.putDouble(ODE_STEP_KEY, h);
    }
}
