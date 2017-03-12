package com.sbutterfly.concurrency;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

/**
 * @author s-ermakov
 */
public class CallbackFuture<V> implements Future<V> {

    private final Future<V> future;
    private final CallbackCallable<V> callable;

    public CallbackFuture(Future<V> future, CallbackCallable<V> callable) {
        this.future = future;
        this.callable = callable;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return future.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return future.isCancelled();
    }

    @Override
    public boolean isDone() {
        return future.isDone();
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        return future.get();
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return future.get(timeout, unit);
    }

    public CallbackFuture<V> setOnSuccess(Consumer<V> onSuccess) {
        this.callable.setOnSuccess(onSuccess);
        return this;
    }

    public CallbackFuture<V> setOnFail(Consumer<Exception> onFail) {
        this.callable.setOnFail(onFail);
        return this;
    }
}
