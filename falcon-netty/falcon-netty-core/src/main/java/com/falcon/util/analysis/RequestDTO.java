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
    /**
     * 目标调用的远程服务方法的参数列表
     */
    private String paramTypeNames;
    /**
     * 调用远程服务方法的参数值，和参数列表11对应
     */
    private List<String> paramValues;
}
