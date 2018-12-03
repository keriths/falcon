package com.falcon.client;

import com.falcon.server.ServerManager;
import com.falcon.server.netty.NettyClient;
import com.falcon.server.servlet.FalconRequest;

/**
 * Created by fanshuai on 15-2-11.
 */
public class FalconCustomerClient {
    private NettyClient client;
    private String host;
    private int port;
    private String protocol;
    public FalconCustomerClient(String host,int port,String protocol){
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        if (ServerManager.TCP.equals(protocol)){
            client = new NettyClient(host,port);
            client.connect();
        }else if (ServerManager.HTTP.equals(protocol)){

        }else {
            throw new RuntimeException(protocol+" client not found");
        }
    }

    public boolean isConnected(){
       return client.isConnected();
    }

    public boolean connect(){
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

    @Override
    public String toString() {
        return host+":"+port;
    }
}
