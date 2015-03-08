package com.fs.falcon.server.hessian;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by fanshuai on 15-1-15.
 */
public class HessianServerConfig {
    private static String IP;
    private static Object IP_LOCK = new Object();
    private static Integer PORT=8080;
    private static String contextPath;
    private static String domain;

    public static String getIP() throws UnknownHostException {
        if(IP==null){
            synchronized (HessianServerConfig.IP_LOCK){
                if(IP==null){
                    IP=InetAddress.getLocalHost().getHostAddress();
                }
            }
        }
        return IP;
    }

    public static Integer getPORT(){
        return PORT;
    }

    public static String getContextPath(){
        return contextPath;
    }
    public static void setContextPath(String contextPath){
        HessianServerConfig.contextPath = contextPath;
    }

    public static String getDomain() {
        return domain;
    }

    public static void setDomain(String domain) {
        HessianServerConfig.domain = domain;
    }
}
