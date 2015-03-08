package com.fs.falcon.client;

import java.util.Random;

/**
 * Created by fanshuai on 15-1-24.
 */
public class NettyClientTest {

    public static void main(String[] args) throws InterruptedException {

        final NettyClient c = new NettyClient();
        c.connect();
        for(int i = 0;i<100;i++){
                    c.sendMsg(i+"");

            //Thread.sleep(1);
            //c.close();
        }
       // c.close();
    }
}
