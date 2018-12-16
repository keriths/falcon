package com.falcon.server.servlet;

import com.falcon.util.DateUtils;

import java.io.Serializable;

/**
 * Created by fanshuai on 15-2-10.
 */
public class FalconResponse implements Serializable{
//    private Throwable throwable;
    private long sequence;
    private Object retObject;
    private String errorMsg;
    private long invokeStartTime = 0;
    private long invokeEndTime = 0;

    public long getSequence() {
        return sequence;
    }

    public void setSequence(long sequence) {
        invokeStartTime = DateUtils.getCurTimeStamp();
        this.sequence = sequence;
    }

    public Object getRetObject() {
        return retObject;
    }

    public void setRetObject(Object retObject) {
        invokeEndTime = DateUtils.getCurTimeStamp();
        this.retObject = retObject;
    }

//    public Throwable getThrowable() {
//        return throwable;
//    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        invokeEndTime = DateUtils.getCurTimeStamp();
        this.errorMsg = errorMsg;
    }

    public long getInvokeStartTime() {
        return invokeStartTime;
    }

    public void setInvokeStartTime(long invokeStartTime) {
        this.invokeStartTime = invokeStartTime;
    }

    public long getInvokeEndTime() {
        return invokeEndTime;
    }

    public void setInvokeEndTime(long invokeEndTime) {
        this.invokeEndTime = invokeEndTime;
    }

    @Override
    public String toString() {
        return "seq:"+getSequence()+" errorMsg:"+errorMsg+" invokeStartTime:"+invokeStartTime+" invokeEndTime:"+invokeEndTime+" useTime:"+(invokeEndTime-invokeStartTime);
    }
}
