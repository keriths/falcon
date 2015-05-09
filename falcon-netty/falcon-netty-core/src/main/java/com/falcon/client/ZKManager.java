package com.falcon.client;

import com.falcon.util.EnvProperties;

/**
 * Created by fanshuai on 15/3/27.
 */
public class ZKManager {
    private static String zkAddress = EnvProperties.get("zk.address");
    public static String getZKAddress(){
        return zkAddress;
    }
}
