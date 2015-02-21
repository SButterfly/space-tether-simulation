package com.sbutterfly.helpers;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Sergei on 31.01.2015.
 */
public class Log {
    private static SimpleDateFormat format;
    static {
        format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
        format.setTimeZone(TimeZone.getTimeZone("Etc/GMT-4"));
    }

    public static void write(LogLvl loglvl, String tag, String message) {

        String time = format.format(new Date());
        String lvl = loglvl.toString();
        PrintStream stream;
        if (loglvl == LogLvl.Error || loglvl == LogLvl.Warning){
            stream = System.err;
        }else{
            stream = System.out;
        }

        String resultString = String.format("%s [%s] [%s]: %s", time, lvl, tag, message);
        stream.println(resultString);
    }

    public static void write(LogLvl logLvl, String tag, String format, Object... args) {
        write(logLvl, tag, String.format(format, args));
    }

    public static void debug(String tag, String message) {
        write(LogLvl.Debug, tag, message);
    }

    public static void debug(String tag, String format, Object... args) {
        write(LogLvl.Debug, tag, format, args);
    }

    public static void debug(Object tagObj, String message) {
        debug(getObjectTag(tagObj), message);
    }

    public static void debug(Object tagObj, String format, Object... args) {
        debug(getObjectTag(tagObj), format, args);
    }

    public static void info(String tag, String message) {
        write(LogLvl.Info, tag, message);
    }

    public static void info(String tag, String format, Object... args) {
        write(LogLvl.Info, tag, format, args);
    }

    public static void info(Object tagObj, String message) {
        info(getObjectTag(tagObj), message);
    }

    public static void info(Object tagObj, String format, Object... args) {
        info(getObjectTag(tagObj), format, args);
    }

    public static void warning(String tag, String message) {
        write(LogLvl.Warning, tag, message);
    }

    public static void warning(String tag, String format, Object... args) {
        write(LogLvl.Warning, tag, format, args);
    }

    public static void warning(Object tagObj, String message) {
        warning(getObjectTag(tagObj), message);
    }

    public static void warning(Object tagObj, String format, Object... args) {
        warning(getObjectTag(tagObj), format, args);
    }

    public static void error(String tag, String message) {
        write(LogLvl.Error, tag, message);
    }

    public static void error(String tag, String format, Object... args) {
        write(LogLvl.Error, tag, format, args);
    }

    public static void error(Object tagObj, String message) {
        error(getObjectTag(tagObj), message);
    }

    public static void error(Object tagObj, String format, Object... args) {
        error(getObjectTag(tagObj), format, args);
    }

    public static void error(Object tagObj, Throwable e) {
        error(tagObj, "An throwable was caught: " + e.getMessage());
        e.printStackTrace();
    }

    public static LogTime recordWorking(String tag){
        return new LogTime(tag);
    }

    public static LogTime recordWorking(Object tag){
        return new LogTime(getObjectTag(tag));
    }

    private static String getObjectTag(Object obj){
        return obj.getClass().getSimpleName();
    }

    enum LogLvl {
        Debug,
        Info,
        Warning,
        Error
    }

    public static class LogTime implements AutoCloseable {

        private static final Object lock = new Object();
        private static int ID = 0;
        private String tag;
        private Stopwatch stopwatch;
        private int id = -1;

        {
            synchronized (lock) {
                id = ID++;
            }
        }
        public LogTime(String tag) {
            stopwatch = new Stopwatch();
            this.tag = tag;
            debug(this.tag, "Start timing! ID: " + id);
        }

        public int getId() {
            return id;
        }

        public Date getStartTime(){
            return stopwatch.getStartTime();
        }

        public Date getElapsedTime(){
            return stopwatch.getElapsedTime();
        }

        @Override
        public void close() {
            debug(this.tag, "End timing! %s ID: %d", stopwatch, id);
            tag = null;
            stopwatch = null;
        }
    }
}
