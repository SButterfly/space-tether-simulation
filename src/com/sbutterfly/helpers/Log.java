package com.sbutterfly.helpers;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Sergei on 31.01.2015.
 */
public class Log {
    enum LogLvl{
        Debug,
        Info,
        Warning,
        Error
    }
    private static SimpleDateFormat format;

    static {
        format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
        format.setTimeZone(TimeZone.getTimeZone("Etc/GMT-4"));
    }

    public static void Write(LogLvl loglvl, String tag, String message){

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

    public static void Write(LogLvl logLvl, String tag, String format, Object... args){
        Write(logLvl, tag, String.format(format, args));
    }

    public static void Debug(String tag, String message){
        Write(LogLvl.Debug, tag, message);
    }
    public static void Debug(String tag, String format, Object... args){
        Write(LogLvl.Debug, tag, format, args);
    }
    public static void Debug(Object tagObj, String message){
        Debug(getObjectTag(tagObj), message);
    }
    public static void Debug(Object tagObj, String format, Object... args){
        Debug(getObjectTag(tagObj), format, args);
    }

    public static void Info(String tag, String message){
        Write(LogLvl.Info, tag, message);
    }
    public static void Info(String tag, String format, Object... args){
        Write(LogLvl.Info, tag, format, args);
    }
    public static void Info(Object tagObj, String message){
        Info(getObjectTag(tagObj), message);
    }
    public static void Info(Object tagObj, String format, Object... args){
        Info(getObjectTag(tagObj), format, args);
    }

    public static void Warning(String tag, String message){
        Write(LogLvl.Warning, tag, message);
    }
    public static void Warning(String tag, String format, Object... args){
        Write(LogLvl.Warning, tag, format, args);
    }
    public static void Warning(Object tagObj, String message){
        Warning(getObjectTag(tagObj), message);
    }
    public static void Warning(Object tagObj, String format, Object... args){
        Warning(getObjectTag(tagObj), format, args);
    }

    public static void Error(String tag, String message){
        Write(LogLvl.Error, tag, message);
    }
    public static void Error(String tag, String format, Object... args){
        Write(LogLvl.Error, tag, format, args);
    }
    public static void Error(Object tagObj, String message){
        Error(getObjectTag(tagObj), message);
    }
    public static void Error(Object tagObj, String format, Object... args){
        Error(getObjectTag(tagObj), format, args);
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

    public static class LogTime implements AutoCloseable {

        private static int ID = 0;
        private static final Object lock = new Object();

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
            Debug(this.tag, "Start timing! ID: " + id);
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
            Debug(this.tag, "End timing! %s ID: %d", stopwatch, id);
            tag = null;
            stopwatch = null;
        }
    }
}
