package com.falcon.client;

import com.falcon.server.ServerManager;

/**
 * Created by fanshuai on 15-2-11.
 */
public class CustomerConfig {
    private String domain;
//    @Deprecated
//    private Class serviceInterface;
    private String serviceId;
    private String protocol = ServerManager.TCP;
    private String group = "default";
    private long timeout = 3000;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
//
//    @Deprecated
//    public Class getServiceInterface() {
//        return serviceInterface;
//    }
//
//    @Deprecated
//    public void setServiceInterface(Class serviceInterface) {
//        this.serviceInterface = serviceInterface;
//    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public String getCustomerInfo(){
        return domain+"&"+getServiceId();
    }
}
