package com.falcon.client;

import com.falcon.server.servlet.FalconRequest;

/**
 * Created by fanshuai on 18/12/16.
 */
public interface InvokeClient {
    public Object doRequest(FalconRequest request, InvokerContext invokerContext);
    public boolean isConnected()    ;
    public  void connect();
}
