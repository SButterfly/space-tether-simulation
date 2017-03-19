package com.sbutterfly.concurrency;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @author s-ermakov
 */
public class ExecutionUtils {

    private ExecutionUtils() {
    }

    public static  <V> CallbackFuture<V> submit(ExecutorService executorService, Callable<V> callable) {
        CallbackCallable<V> callbackCallable = new CallbackCallable<>(callable);
        Future<V> future = executorService.submit(callbackCallable);
        return new CallbackFuture<>(future, callbackCallable);
    }
}
