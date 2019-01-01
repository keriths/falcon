package com.falcon.util.analysis;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fanshuai on 18/11/20.
 */
public class RequestDTO implements Serializable{
    /**
     * 每次调用的id
     */
    private String sequenceNum;
    /**
     * 整个业务调用生命周期跟踪ID
     */
    private String traceId;
    /**
     * 请求端的appkey
     */
    private String clientAppKey;
    /**
     * 目标远程接口的appKey
     */
    private String serviceAppKey;
    /**
     * 目标调用的远程接口的服务名
     */
    private String serviceName;
    /**
     * 目标调用的远程服务的方法名
     */
    private String methodName;
    private String methodId;
//    /**
//     * 目标调用的远程服务方法的参数列表
//     */
//    private String paramTypeNames;
    /**
     * 调用远程服务方法的参数值，和参数列表11对应
     */
    private Object[] paramValues;


    public String getSequenceNum() {
        return sequenceNum;
    }

    public void setSequenceNum(String sequenceNum) {
        this.sequenceNum = sequenceNum;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getClientAppKey() {
        return clientAppKey;
    }

    public void setClientAppKey(String clientAppKey) {
        this.clientAppKey = clientAppKey;
    }

    public String getServiceAppKey() {
        return serviceAppKey;
    }

    public void setServiceAppKey(String serviceAppKey) {
        this.serviceAppKey = serviceAppKey;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

//    public String getParamTypeNames() {
//        return paramTypeNames;
//    }

//    public void setParamTypeNames(String paramTypeNames) {
//        this.paramTypeNames = paramTypeNames;
//    }

    public Object[] getParamValues() {
        return paramValues;
    }

    public void setParamValues(Object[] paramValues) {
        this.paramValues = paramValues;
    }

    public String getMethodId() {
        return methodId;
    }

    public void setMethodId(String methodId) {
        this.methodId = methodId;
    }
}
