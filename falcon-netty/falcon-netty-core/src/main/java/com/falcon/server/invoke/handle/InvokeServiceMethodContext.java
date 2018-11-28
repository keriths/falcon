package com.falcon.server.invoke.handle;

import com.falcon.server.servlet.FalconRequest;
import com.falcon.util.analysis.ServiceMethodStructureInfo;

/**
 * Created by fanshuai on 18/11/28.
 */
public class InvokeServiceMethodContext {

    private ServiceMethodStructureInfo methodStructureInfo;
    private FalconRequest falconRequest;
    private String protocol;

    private Object retValue;


    public ServiceMethodStructureInfo getMethodStructureInfo() {
        return methodStructureInfo;
    }

    public void setMethodStructureInfo(ServiceMethodStructureInfo methodStructureInfo) {
        this.methodStructureInfo = methodStructureInfo;
    }

    public Object getRetValue() {
        return retValue;
    }

    public void setRetValue(Object retValue) {
        this.retValue = retValue;
    }

    public FalconRequest getFalconRequest() {
        return falconRequest;
    }

    public void setFalconRequest(FalconRequest falconRequest) {
        this.falconRequest = falconRequest;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
