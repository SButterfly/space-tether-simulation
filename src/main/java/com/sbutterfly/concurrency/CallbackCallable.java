package com.sbutterfly.concurrency;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

/**
 * Класс. обертка, позволяющий следить за состояниями Callable.
 *
 * @author s-ermakov
 */
public class CallbackCallable<T> implements Callable<T> {

    private final Callable<T> callable;

    private Consumer<T> onSuccess;
    private Consumer<Exception> onFail;

    private T result;
    private Exception exception;

    public CallbackCallable(Callable<T> callable) {
        this.callable = callable;
    }

    public synchronized void setOnSuccess(Consumer<T> onSuccess) {
        this.onSuccess = onSuccess;
        if (result != null && onSuccess != null) {
            onSuccess.accept(result);
        }
    }

    public synchronized void setOnFail(Consumer<Exception> onFail) {
        this.onFail = onFail;
        if (exception != null && onFail != null) {
            onFail.accept(exception);
        }
    }

    @Override
    public T call() throws Exception {
        try {
            T resultCall = callable.call();
            onSuccess(resultCall);
            return resultCall;
        } catch (Exception e) {
            onFail(e);
            throw e;
        }
    }

    private synchronized void onSuccess(T result) {
        this.result = result;
        if (onSuccess != null) {
            onSuccess.accept(result);
        }
    }

    private synchronized void onFail(Exception exception) {
        this.exception = exception;
        if (onFail != null) {
            onFail.accept(exception);
        }
    }
}
