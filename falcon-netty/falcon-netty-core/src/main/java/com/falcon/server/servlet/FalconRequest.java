package com.falcon.server.servlet;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by fanshuai on 15-2-10.
 */
public class FalconRequest implements Serializable{
    private String domain;
    private String serviceId;
    private String serviceMethod;
    private Object[] parameters;
//    private String parameterTypeNames;
    private String methodId;

    public String getMethodId() {
        return methodId;
    }

    public void setMethodId(String methodId) {
        this.methodId = methodId;
    }

    private long sequence = System.currentTimeMillis()*10000000000l+(long)(new Random().nextInt(999999999));

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceMethod() {
        return serviceMethod;
    }

    public void setServiceMethod(String serviceMethod) {
        this.serviceMethod = serviceMethod;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public long getSequence() {
        return sequence;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }





    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }


    @Override
    public String toString() {
        return "seq:"+getSequence()+" method:"+getServiceMethod()+" serviceId:"+ getServiceId();
    }


//    public String getParameterTypeNames() {
//        return parameterTypeNames;
//    }
//
//    public void setParameterTypeNames(String parameterTypeNames) {
//        this.parameterTypeNames = parameterTypeNames;
//    }
}
