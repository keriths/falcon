package com.falcon.regist;

import com.alibaba.fastjson.JSON;
import com.falcon.config.ProviderConfig;
import com.falcon.config.ProviderZKNodeConfig;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Created by fanshuai on 15-2-7.
 */
public class ServiceRegistManager {
    private static ZKClient zkClient ;
    public static ZKClient getZKClient() throws Exception {
        if(zkClient==null){
            synchronized (ServiceRegistManager.class){
                if(zkClient==null){
                    zkClient = new ZKClient();
                }
            }
        }
        return zkClient;
    }

    public static final String serviceBaseNodePath="/falcon/Server";
    public static void registService(ProviderConfig providerConfig) throws Exception {
        String domain = providerConfig.getDomain();
        String interfaceName=providerConfig.getServiceInterface().getName();
        String pathNode = getServicePathNode(domain,interfaceName,providerConfig.getServerConfig().getIp(),providerConfig.getServerConfig().getPort());
        if(!getZKClient().existsNode(pathNode)){
            ProviderZKNodeConfig nodeValue = new ProviderZKNodeConfig();
            nodeValue.setDomain(domain);
            nodeValue.setService(interfaceName);
            nodeValue.setHost(providerConfig.getServerConfig().getIp());
            nodeValue.setPort(providerConfig.getServerConfig().getPort());
            nodeValue.setProtocol(providerConfig.getServerConfig().getProtocol());
            nodeValue.setGroup(providerConfig.getGroup());
            nodeValue.setWeight(1);
            String jsonValue = JSON.toJSONString(nodeValue);
            getZKClient().createNodeWithEPHEMERAL(pathNode, jsonValue.getBytes());
        }
    }

    public static String getServicePathNode(String domain,String interfaceName,String host,int port){
        return getServiceProvidersPathNode(domain,interfaceName)+"/"+host+":"+port;
    }
    public static String getServiceProvidersPathNode(String domain,String interfaceName){
        return serviceBaseNodePath+"/"+domain+"/"+interfaceName+"/"+"providers";
    }

}
