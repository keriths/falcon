package com.falcon.client;

import com.falcon.server.servlet.FalconResponse;

/**
 * Created by fanshuai on 15-2-11.
 */
public class InvokerCallBack {
    private long timeOut = 3000;
    private boolean hasComplace = false;
    private Object returnObject;
    private boolean hasError = false;
    private Throwable throwable;
    public void processResponse(FalconResponse response){
        synchronized (this){
            returnObject = response.getRetObject();
            hasComplace = true;
            notifyAll();
        }
    }

    public Object get() throws Exception {
        synchronized (this){
            long lastTime = timeOut;
            while (!hasComplace){
                try {
                    if(lastTime<=0){
                        throw new Exception("time out");
                    }
                    wait(timeOut);
                    lastTime -= timeOut;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (hasError){
                throw new Exception(throwable);
            }
        }
        return returnObject;

    }

    public void processFailedResponse(Throwable cause) {
        synchronized (this) {
            this.throwable = cause;
            hasComplace = true;
            hasError = true;
            notifyAll();
        }
    }
}
