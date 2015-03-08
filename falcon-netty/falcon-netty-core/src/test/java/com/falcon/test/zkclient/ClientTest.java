package com.falcon.test.zkclient;

import com.falcon.config.ProviderConfig;
import com.falcon.config.ServerConfig;
import com.falcon.regist.ServiceRegistManager;
import com.falcon.test.api.FalconTestService;

/**
 * Created by fanshuai on 15-2-9.
 */
public class ClientTest {

    public static void main(String [] args) throws Exception {
        ProviderConfig p = new ProviderConfig();
        p.setDomain("falcon");
        p.setServiceInterface(FalconTestService.class);
        ServerConfig s = new ServerConfig();
        s.setIp("192.168.1.1");
        s.setPort(1112);
        p.setServerConfig(s);
        ServiceRegistManager.registService(p);
        Thread.sleep(1000000);
    }
}
