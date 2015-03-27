package com.falcon.spring;

import com.falcon.client.CustomerConfig;
import com.falcon.client.CustomerManager;
import com.falcon.server.servlet.FalconRequest;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by fanshuai on 15-2-11.
 */
public class FalconServiceFactory implements FactoryBean{

    /**
     * 项目名称
     */
    private String domain;
    /**
     * 接口
     */
    private Class serviceInterface;
    /**
     * 超时时间
     */
    private long timeOut;
    /**
     * 分组
     */
    private String group = "default";
    @Override
    public Object getObject() throws Exception {
        CustomerConfig customerConfig = new CustomerConfig();
        customerConfig.setDomain(domain);
        customerConfig.setServiceInterface(serviceInterface);
        customerConfig.setGroup(group);
        CustomerManager.addCustomer(customerConfig);
        return Proxy.newProxyInstance(FactoryBean.class.getClassLoader(),new Class[]{serviceInterface},new FalconServiceInvocationHandler(customerConfig));
    }

    class FalconServiceInvocationHandler implements InvocationHandler{
        private CustomerConfig customerConfig;
        public FalconServiceInvocationHandler(CustomerConfig customerConfig){
            this.customerConfig = customerConfig;
        }
        @Override
        public Object invoke(Object proxy, Method method, Object[] parameters) throws Throwable {
            FalconRequest request = new FalconRequest();
            request.setParameters(parameters);
            request.setServiceMethod(method.getName());
            request.setParameterTypes(method.getParameterTypes());
            request.setServiceInterfaceName(customerConfig.getServiceInterface().getName());
            return CustomerManager.invoke(request,customerConfig);
        }
    }
    @Override
    public Class<?> getObjectType() {
        return serviceInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }


    public Class getServiceInterface() {
        return serviceInterface;
    }

    public void setServiceInterface(Class serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
