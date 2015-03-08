package com.fs.falcon.server.hessian;

import com.alibaba.fastjson.JSONObject;
import com.fs.falcon.zk.ZKClient;
import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.remoting.caucho.HessianServiceExporter;

import java.net.UnknownHostException;

/**
 * Created by fanshuai on 15-1-11.
 */
public class HessianServiceProvider extends HessianServiceExporter implements InitializingBean{
    private String group="default";
    private String version="1.0";
    private String domain;
    private String serverPath;
    private Integer port = 8080;

    public String getServerPath() {
        return serverPath;
    }

    public void setServerPath(String serverPath) {
        this.serverPath = serverPath;
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

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public void afterPropertiesSet()  {
        super.afterPropertiesSet();
        try {
            ProviderConfig providerConfig = new ProviderConfig(HessianServerConfig.getDomain(),getServiceInterface(),HessianServerConfig.getIP(),HessianServerConfig.getPORT(),HessianServerConfig.getContextPath(),getServerPath());
            ZKClient.getInstance().addServerProvider(providerConfig.getZookeeperServerNodeName(),JSONObject.toJSONString(providerConfig));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }
}
