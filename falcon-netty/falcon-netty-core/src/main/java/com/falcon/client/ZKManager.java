package com.falcon.client;

/**
 * Created by fanshuai on 15/3/27.
 */
public class ZKManager {
    private static String zkAddress;
    static {
        String address = System.getProperty("falcon.zk.address");
        if (address==null) {
            throw new RuntimeException("please setting \"zk.address\" in vm properties with -D ");
        }
        zkAddress = address;
    }
    public static String getZKAddress(){
        return zkAddress;
    }
}
