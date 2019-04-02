package com.cgltech.cat_conn.server.cat.future;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by fuzhengwei1 on 2016/10/20.
 * @param <T>
 */
public class SyncWriteFuture<T>  implements WriteFuture<T>  {

    private CountDownLatch latch = new CountDownLatch(1);
    private final long begin = System.currentTimeMillis();
    private long timeout;
    private T response;
    private boolean writeResult;
    private Throwable cause;
    private boolean isTimeout = false;

    public SyncWriteFuture() {
    }

    public SyncWriteFuture(String requestId, long timeout) {
        this.timeout = timeout;
        writeResult = true;
        isTimeout = false;
    }


    public Throwable getCause() {
        return this.cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    public boolean getWriteResult() {
        return this.writeResult;
    }

    public void setWriteResult(boolean result) {
        this.writeResult = result;
    }

    public T getResponse() {
        return this.response;
    }

    public void setResponse(T response) {
        this.response = response;
        latch.countDown();
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        return true;
    }

    public boolean isCancelled() {
        return false;
    }

    public boolean isDone() {
        return false;
    }

    public T get() throws InterruptedException, ExecutionException {
        latch.await();
        return response;
    }

    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (latch.await(timeout, unit)) {
            return this.response;
        }
        return null;
    }

    public boolean isTimeout() {
        if (isTimeout) {
            return isTimeout;
        }
        return System.currentTimeMillis() - begin > timeout;
    }
}
