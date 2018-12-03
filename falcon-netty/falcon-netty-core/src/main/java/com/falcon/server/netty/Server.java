package com.falcon.server.netty;

import java.net.UnknownHostException;

/**
 * Created by fanshuai on 15-1-23.
 */
public interface Server {

    /**
     * 服务提供者IP
     */
    String getIp();
    /**
     * 服务提供者端口
     */
    int getPort();
    /**
     * 协议
     */
    String getProtocol();

    boolean isStarted();

    void start() throws UnknownHostException;

    Object doRequest(Object param);

}
