package com.falcon.client;

import com.falcon.server.netty.NettyClient;
import com.falcon.server.servlet.FalconRequest;

/**
 * Created by fanshuai on 15-2-11.
 */
public class FalconCustomerClient {
    private NettyClient client;
    private String host;
    private int port;
    public FalconCustomerClient(String host,int port){
        this.host = host;
        this.port = port;
        client = new NettyClient(host,port);
        client.connect();
    }

    public boolean isConnected(){
       return client.isConnected();
    }

    public boolean reInitClient(){
        client.close();
        client = new NettyClient(host,port);
        client.connect();
        return client.isConnected();
    }

    public void doRequest(FalconRequest request, InvokerContext invokerContext) {
        client.write(request);
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
