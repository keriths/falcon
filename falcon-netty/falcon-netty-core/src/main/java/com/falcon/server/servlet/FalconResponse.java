package com.falcon.server.servlet;

import java.io.Serializable;

/**
 * Created by fanshuai on 15-2-10.
 */
public class FalconResponse implements Serializable{
    private Throwable throwable;
    private long sequence;
    private Object retObject;
    private String errorMsg;


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

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
