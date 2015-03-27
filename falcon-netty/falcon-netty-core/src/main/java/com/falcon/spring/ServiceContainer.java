package com.falcon.spring;

import com.falcon.config.ProviderConfig;
import com.falcon.config.ServerConfig;
import com.falcon.exception.ServiceContainerInitException;
import com.falcon.regist.ServiceRegistManager;
import com.falcon.server.ServerManager;
import com.falcon.server.ServiceProviderManager;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by fanshuai on 15-2-7.
 */
public class ServiceContainer implements Serializable{
    @Setter
    private String domain;
    @Setter
    private String group = "default";
    @Setter
    private int port = 32526;
    @Setter
    private Map<Class,Object> serviceProviders;

    public void init() throws Exception {
        if(domain==null){
            throw new ServiceContainerInitException("ServiceContainer.domain cannt null");
        }
        if(serviceProviders==null || serviceProviders.isEmpty()){
            throw new ServiceContainerInitException("ServiceContainer.serviceProviders cannt null");
        }
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setPort(port);
        List<ProviderConfig> providerConfigList = new ArrayList<ProviderConfig>();
        for(Map.Entry<Class,Object> entry : serviceProviders.entrySet()){
            ProviderConfig providerConfig = new ProviderConfig();
            providerConfig.setServiceInterface(entry.getKey());
            providerConfig.setService(entry.getValue());
            providerConfig.setGroup(group);
            providerConfig.setDomain(domain);
            providerConfig.setServerConfig(serverConfig);
            providerConfigList.add(providerConfig);
        }

        //解析配置
        ServiceProviderManager.addServiceProvider(providerConfigList);
        //启动服务
        ServerManager.startServer(serverConfig);
        //注册服务
        ServiceProviderManager.registService();
    }
}
