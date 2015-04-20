package com.falcon.client;

/**
 * Created by fanshuai on 15/3/27.
 */
public class ZKManager {
    private static String zkAddress;
    public static String getZKAddress(){
        if(zkAddress==null){
            zkAddress = System.getProperty("falcon.zk.address");
            if(zkAddress==null){
                throw new RuntimeException("please setting \"zk.address\" in vm properties with -D ");
            }
        }
        return zkAddress;
    }
}
