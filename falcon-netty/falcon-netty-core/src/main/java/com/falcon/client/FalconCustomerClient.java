package com.falcon.client;

import com.falcon.server.netty.NettyClient;
import com.falcon.server.servlet.FalconRequest;

/**
 * Created by fanshuai on 15-2-11.
 */
public class FalconCustomerClient {
    private NettyClient client;
    public FalconCustomerClient(String host,int port){
        client = new NettyClient(host,port);
        client.connect();
    }
    public void doRequest(FalconRequest request, InvokerContext invokerContext) {
        client.write(request);
    }
}
