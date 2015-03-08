package com.falcon.server;

import com.falcon.config.ProviderConfig;
import com.falcon.exception.ServiceContainerInitException;
import com.falcon.regist.ServiceRegistManager;
import com.falcon.server.method.ServiceMethod;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by fanshuai on 15-2-7.
 */
public class ServiceProviderManager {
    private static Map<String,ProviderConfig> serviceProviderCache = new HashMap<String, ProviderConfig>();
    private static Map<String,ServiceMethod> serviceMethodCache = new HashMap<String, ServiceMethod>();
    private static Set<String> ignoreMethods = new HashSet<String>();
    static {
        Method[] objectMethodArray = Object.class.getMethods();
        for (Method method : objectMethodArray) {
            ignoreMethods.add(method.getName());
        }
        Method[] classMethodArray = Class.class.getMethods();
        for (Method method : classMethodArray) {
            ignoreMethods.add(method.getName());
        }
    }

    public static void registService() throws Exception {
        for(Map.Entry<String,ProviderConfig> entry:serviceProviderCache.entrySet()){
            ProviderConfig providerConfig = entry.getValue();
            ServiceRegistManager.registService(providerConfig);
        }
    }
    public static void addServiceProvider(List<ProviderConfig> providerConfigList) throws Exception {
        if(providerConfigList==null || providerConfigList.isEmpty()){
            return;
        }
        for(ProviderConfig providerConfig:providerConfigList) {
            addServiceProvider(providerConfig);
        }
    }
    public static void addServiceProvider(ProviderConfig providerConfig) throws Exception {
        Class serviceInterface = providerConfig.getServiceInterface();
        if(serviceProviderCache.containsKey(serviceInterface.getName())){
            throw new ServiceContainerInitException(serviceInterface.getName()+" has init again");
        }
        Method[] methodArray = serviceInterface.getMethods();
        for(Method method:methodArray){
            if(!ignoreMethods.contains(method.getName())){
                ServiceMethod serviceMethod = new ServiceMethod(providerConfig.getService(),method);
                String key = getKey(providerConfig.getServiceInterface().getName(),method.getName(),serviceMethod.getParamNamesStr());
                serviceMethodCache.put(key,serviceMethod);
            }
        }
        serviceProviderCache.put(serviceInterface.getName(),providerConfig);
    }
    private static String getKey(String serviceName,String methodName,String paramNamesStr){
        return serviceName+"#"+methodName+"#"+paramNamesStr;
    }
    public static ServiceMethod getServiceMethod(String serviceName,String methodName,String paramNamesStr){
        String key = getKey(serviceName,methodName,paramNamesStr);
        return serviceMethodCache.get(key);
    }
}
