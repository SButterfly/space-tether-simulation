package com.sbutterfly.services;

import javax.swing.SwingUtilities;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author s-ermakov
 */
public class Execution {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    private Execution() {
    }

    public static ExecutorService getService() {
        return EXECUTOR_SERVICE;
    }

    public static Future<?> submit(Runnable task) {
        return getService().submit(task);
    }

    public static void submitInMain(Runnable runnable) {
        SwingUtilities.invokeLater(runnable);
    }
}
