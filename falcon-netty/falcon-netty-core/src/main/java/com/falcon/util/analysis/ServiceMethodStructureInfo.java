package com.falcon.util.analysis;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务的方法结构信息
 * Created by fanshuai on 18/10/27.
 */
public class ServiceMethodStructureInfo implements Serializable{
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
    private transient Object serviceInstance;


    /**
     * 方法id
     */
    private String methodId;
    /**
     * 方法描述
     */
    private String methodDesc;
    /**
     * 方法名称
     */
    private String methodName;
//    /**
//     * 方法参数列表
//     */
//    private String paramTypes;
    /**
     * 具体方法
     */
    private transient Method method;
    /**
     * 返回的类型
     */
    private String returnClassName;
    /**
     * 方法的参数描述
     */
    private LinkedHashMap<String,ParamStructure> paramStructureMap;
    /**
     * 类属性结构集合
     */
    private Map<String,ClassPropertiesStructure> classPropertiesStructureMap;


    public String getMethodId() {
        return methodId;
    }

    public void setMethodId(String methodId) {
        this.methodId = methodId;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }


    public LinkedHashMap<String, ParamStructure> getParamStructureMap() {
        return paramStructureMap;
    }

    public void setParamStructureMap(LinkedHashMap<String, ParamStructure> paramStructureMap) {
        this.paramStructureMap = paramStructureMap;
    }

    public Map<String, ClassPropertiesStructure> getClassPropertiesStructureMap() {
        return classPropertiesStructureMap;
    }

    public void setClassPropertiesStructureMap(Map<String, ClassPropertiesStructure> classPropertiesStructureMap) {
        this.classPropertiesStructureMap = classPropertiesStructureMap;
    }

    public String getMethodDesc() {
        return methodDesc;
    }

    public void setMethodDesc(String methodDesc) {
        this.methodDesc = methodDesc;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getReturnClassName() {
        return returnClassName;
    }

    public void setReturnClassName(String returnClassName) {
        this.returnClassName = returnClassName;
    }

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

//    public String getParamTypes() {
//        return paramTypes;
//    }
//
//    public void setParamTypes(String paramTypes) {
//        this.paramTypes = paramTypes;
//    }
}
