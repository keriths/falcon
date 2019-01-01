package com.falcon.spring;

import com.falcon.client.CustomerConfig;
import com.falcon.client.CustomerManager;
import com.falcon.server.ServerManager;
import com.falcon.server.servlet.FalconRequest;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by fanshuai on 15-2-11.
 */
public class FalconServiceFactory implements FactoryBean,InitializingBean{

    private String domain;
    private Class serviceInterface;
    private String serviceId;
    private String group = "default";
    private String protocol = ServerManager.TCP;
    private CustomerConfig customerConfig ;
    private long timeOut = 3000;
    @Override
    public Object getObject() throws Exception {
        return Proxy.newProxyInstance(FactoryBean.class.getClassLoader(),new Class[]{serviceInterface},new FalconServiceInvocationHandler(customerConfig));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        customerConfig = new CustomerConfig();
        customerConfig.setDomain(domain);
//        customerConfig.setServiceInterface(serviceInterface);
        customerConfig.setServiceId(serviceId);
        customerConfig.setGroup(group);
        customerConfig.setTimeout(timeOut);
        customerConfig.setProtocol(protocol);
        CustomerManager.addCustomer(customerConfig);
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
//            request.setParameterTypeNames(getParamTypesName(method.getParameterTypes()));
            request.setMethodId(method.getName()+getParamTypesName(method.getParameterTypes()));
            request.setServiceId(customerConfig.getServiceId());
            return CustomerManager.invoke(request,customerConfig);
        }
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getParamTypesName(Class[] parameterTypes){
        if (parameterTypes == null || parameterTypes.length == 0){
            return "()";
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append("(");
        for (Class c : parameterTypes)
        {
            buffer.append(c.getName()).append(",");
        }
        return buffer.substring(0,buffer.length()-1)+")";
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

    public long getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(long timeOut) {
        this.timeOut = timeOut;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
