package com.fs.falcon.client;

import com.fs.falcon.zk.ReadProviderConfigWatcher;
import com.fs.falcon.zk.ZKClient;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;
import java.util.List;

/**
 * Created by fanshuai on 15-1-15.
 */
public class FalconHessianServiceClientFactory implements FactoryBean {
    private Class serviceInteface;
    private String domain;
    public String group;
    public String version;

    @Override
    public Object getObject() throws Exception {
        ZKClient z = ZKClient.getInstance();
        List<String> serviceConfigs = z.getServiceProviders(domain+"_"+serviceInteface.getName(),new ReadProviderConfigWatcher());
        ServiceProviders.addConfigs(domain+"_"+serviceInteface.getName(),serviceConfigs);
        ServiceHessianInvocationHandler hessianInvocationHandler = new ServiceHessianInvocationHandler(domain,serviceInteface);
        return Proxy.newProxyInstance(FalconHessianServiceClientFactory.class.getClassLoader(), new Class[]{serviceInteface}, hessianInvocationHandler);
    }

    @Override
    public Class<?> getObjectType() {
        return serviceInteface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }


    public Class getServiceInteface() {
        return serviceInteface;
    }

    public void setServiceInteface(Class serviceInteface) {
        this.serviceInteface = serviceInteface;
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
