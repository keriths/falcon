package com.falcon.server.netty;

import java.net.UnknownHostException;

/**
 * Created by fanshuai on 15-1-23.
 */
public interface Server {

    boolean isStarted();

    void start() throws UnknownHostException;

    Object doRequest(Object param);

}
