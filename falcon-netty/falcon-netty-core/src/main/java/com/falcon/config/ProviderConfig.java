package com.falcon.config;

import com.falcon.server.ServerManager;
import lombok.Data;
import lombok.Setter;

/**
 * Created by fanshuai on 15-2-7.
 */
public class ProviderConfig<T> {
    private String protocol = ServerManager.TCP;
    /**
     * 服务api接口
     */
//    private Class<T> serviceInterface;
    private String serviceId;
    /**
     * 服务的实现
     */
    private T service;
    /**
     * 当前服务器配置信息
     */
    private ServerConfig serverConfig;
    /**
     * domain
     */
    @Setter
    private String domain;
    /**
     * 分组
     */
    @Setter
    private String group = "default";


    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

//    public Class<T> getServiceInterface() {
//        return serviceInterface;
//    }
//
//    public void setServiceInterface(Class<T> serviceInterface) {
//        this.serviceInterface = serviceInterface;
//    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public T getService() {
        return service;
    }

    public void setService(T service) {
        this.service = service;
    }

    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public void setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public String getDomain() {
        return domain;
    }

    public String getGroup() {
        return group;
    }
}
