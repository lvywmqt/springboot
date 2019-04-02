package com.cgltech.cat_conn.server.cat.future;


import java.util.concurrent.Future;

/**
 * Created by fuzhengwei1 on 2016/10/20.
 */
public interface WriteFuture<T> extends Future<T> {

    Throwable getCause();

    void setCause(Throwable cause);

    boolean getWriteResult();

    void setWriteResult(boolean result);

    T getResponse();
    void setResponse(T response);

    boolean isTimeout();


}
