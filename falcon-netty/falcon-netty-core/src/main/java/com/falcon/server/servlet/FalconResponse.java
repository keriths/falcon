package com.falcon.server.servlet;

import java.io.Serializable;

/**
 * Created by fanshuai on 15-2-10.
 */
public class FalconResponse implements Serializable{
    private Throwable throwable;
    private long sequence;
    private Object retObject;


    public long getSequence() {
        return sequence;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }

    public Object getRetObject() {
        return retObject;
    }

    public void setRetObject(Object retObject) {
        this.retObject = retObject;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
