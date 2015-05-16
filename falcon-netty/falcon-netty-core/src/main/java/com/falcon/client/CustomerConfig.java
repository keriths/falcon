package com.falcon.client;

/**
 * Created by fanshuai on 15-2-11.
 */
public class CustomerConfig {
    private String domain;
    private Class serviceInterface;
    private String group = "default";
    private long timeout = 3000;


    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Class getServiceInterface() {
        return serviceInterface;
    }

    public void setServiceInterface(Class serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

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
        return domain+"&"+serviceInterface.getName();
    }
}
