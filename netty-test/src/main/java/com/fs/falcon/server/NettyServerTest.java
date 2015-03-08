package com.fs.falcon.server;

import java.net.UnknownHostException;

/**
 * Created by fanshuai on 15-1-24.
 */
public class NettyServerTest {

    public static void main(String[] args){
        NettyServer server = new NettyServer();
        try {
            server.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
