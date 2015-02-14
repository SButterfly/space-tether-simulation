package com.sbutterfly.helpers;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sergei on 01.02.2015.
 */
public class Stopwatch {

    private long startTime;
    private static SimpleDateFormat format = new SimpleDateFormat("mm:ss.SSS");

    public Stopwatch() {
        startTime = System.currentTimeMillis();
    }

    public Date getStartTime() {
        return new Date(startTime);
    }

    public Date getElapsedTime(){
        return new Date(System.currentTimeMillis() - startTime);
    }

    @Override
    public String toString() {
        return "ElapsedTime: " + format.format(getElapsedTime());
    }
}
