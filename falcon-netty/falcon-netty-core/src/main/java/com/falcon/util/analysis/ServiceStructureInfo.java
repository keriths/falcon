package com.falcon.util.analysis;

import java.io.Serializable;
import java.util.List;

/**
 * 解析的服务结构
 * Created by fanshuai on 18/10/27.
 */
public class ServiceStructureInfo implements Serializable{
    /**
     * 服务id
     */
    private String serviceId;
    /**
     * 服务类的完整路径
     */
    private String serviceTypeName;
    /**
     * 服务描述，用于生成文档
     */
    private String serviceDesc;
    /**
     * 服务的具体实现
     */
    private transient Object serviceInstance;
    /**
     * 当前服务提供的所有方法列表
     */
    private List<ServiceMethodStructureInfo> serviceMethodStructureInfos;


    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceTypeName() {
        return serviceTypeName;
    }

    public void setServiceTypeName(String serviceTypeName) {
        this.serviceTypeName = serviceTypeName;
    }

    public String getServiceDesc() {
        return serviceDesc;
    }

    public void setServiceDesc(String serviceDesc) {
        this.serviceDesc = serviceDesc;
    }

    public Object getServiceInstance() {
        return serviceInstance;
    }

    public void setServiceInstance(Object serviceInstance) {
        this.serviceInstance = serviceInstance;
    }

    public List<ServiceMethodStructureInfo> getServiceMethodStructureInfos() {
        return serviceMethodStructureInfos;
    }

    public void setServiceMethodStructureInfos(List<ServiceMethodStructureInfo> serviceMethodStructureInfos) {
        this.serviceMethodStructureInfos = serviceMethodStructureInfos;
    }
}
