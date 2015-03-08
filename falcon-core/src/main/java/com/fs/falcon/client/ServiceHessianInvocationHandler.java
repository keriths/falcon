package com.fs.falcon.client;

import com.caucho.hessian.client.HessianProxyFactory;
import com.fs.falcon.server.hessian.ProviderConfig;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by fanshuai on 15-1-13.
 */
public class ServiceHessianInvocationHandler implements InvocationHandler{
    public ServiceHessianInvocationHandler(String domain, Class service){
        this.domain = domain;
        this.service = service;
    }
    public Class service;
    public String domain;
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ProviderConfig serviceConfig = ServiceProviders.getServiceProviderConfig(domain, service);
        if(serviceConfig==null){
            throw new Throwable("未找到服务:"+domain+"_"+service.getName());
        }
        HessianProxyFactory factory = new HessianProxyFactory();
        Object serviceImpl = factory.create(service,serviceConfig.getHesianUrl());
        return method.invoke(serviceImpl,args);
    }
}
