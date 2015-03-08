package com.falcon.client;

/**
 * Created by fanshuai on 15-2-11.
 */
public class CustomerConfig {
    private String domain;
    private Class serviceInterface;
    private String group = "default";


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
}
