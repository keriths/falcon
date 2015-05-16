package com.falcon.server.servlet;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by fanshuai on 15-2-10.
 */
public class FalconRequest implements Serializable{
    private String domain;
    private String serviceInterfaceName;
    private String serviceMethod;
    private Object[] parameters;
    private Class[] parameterTypes;
    private long sequence = System.currentTimeMillis()*1000+(new Random().nextInt(999));

    public String getServiceInterfaceName() {
        return serviceInterfaceName;
    }

    public void setServiceInterfaceName(String serviceInterfaceName) {
        this.serviceInterfaceName = serviceInterfaceName;
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

    public Class[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getRequestInfo(){
        return "seq:" + getSequence()+ " domain:" +getDomain() +" service:"+getServiceInterfaceName() +" method"+getServiceMethod();
    }
}
