package com.falcon.regist;

import com.alibaba.fastjson.JSON;
import com.falcon.config.ProviderConfig;
import com.falcon.config.ProviderZKNodeConfig;
import com.falcon.server.ServerManager;
import com.falcon.server.netty.Server;
import com.falcon.util.AppDomainNameUtils;
import com.falcon.util.analysis.ServiceStructureInfo;

import java.util.List;

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
//        String domain = providerConfig.getDomain();
        String serviceId=providerConfig.getServiceId();
        registService(providerConfig.getDomain(),serviceId,providerConfig.getServerConfig().getProtocol(),providerConfig.getServerConfig().getIp(),providerConfig.getServerConfig().getPort(),providerConfig.getGroup());
//
//        String pathNode = getServicePathNode(domain,interfaceName,providerConfig.getServerConfig().getIp(),providerConfig.getServerConfig().getPort());
//        if(getZKClient().existsNode(pathNode)) {
//            long sessionId = getZKClient().getSessionId();
//            long owner = getZKClient().nodeStat(pathNode).getEphemeralOwner();
//            if(owner==sessionId){
//                //已经存在自己创建的临时节点，直接返回，否则删除重建
//                return ;
//            }
//            getZKClient().deleteNode(pathNode);
//        }
//        ProviderZKNodeConfig nodeValue = new ProviderZKNodeConfig();
//        nodeValue.setDomain(domain);
//        nodeValue.setServiceId(interfaceName);
//        nodeValue.setHost(providerConfig.getServerConfig().getIp());
//        nodeValue.setPort(providerConfig.getServerConfig().getPort());
//        nodeValue.setProtocol(providerConfig.getServerConfig().getProtocol());
//        nodeValue.setGroup(providerConfig.getGroup());
//        nodeValue.setWeight(1);
//        String jsonValue = JSON.toJSONString(nodeValue);
//        getZKClient().createNodeWithEPHEMERAL(pathNode, jsonValue.getBytes());
    }
    public static void registService(String domain,String serviceId,String protocol,String ip,int port,String group) throws Exception {
//        String domain = providerConfig.getDomain();
//        String interfaceName=serviceName;
        String pathNode = getServicePathNode(domain,serviceId,protocol,ip,port);
        if(getZKClient().existsNode(pathNode)) {
            long sessionId = getZKClient().getSessionId();
            long owner = getZKClient().nodeStat(pathNode).getEphemeralOwner();
            if(owner==sessionId){
                //已经存在自己创建的临时节点，直接返回，否则删除重建
                return ;
            }
            getZKClient().deleteNode(pathNode);
        }
        ProviderZKNodeConfig nodeValue = new ProviderZKNodeConfig();
        nodeValue.setDomain(domain);
        nodeValue.setServiceId(serviceId);
        nodeValue.setHost(ip);
        nodeValue.setPort(port);
        nodeValue.setProtocol(protocol);
        nodeValue.setGroup(group);
        nodeValue.setWeight(1);
        String jsonValue = JSON.toJSONString(nodeValue);
        getZKClient().createNodeWithEPHEMERAL(pathNode, jsonValue.getBytes());
    }

    public static String getServicePathNode(String domain,String interfaceName,String protocol,String host,int port){
        return getServiceProvidersPathNode(domain,interfaceName,protocol)+"/"+host+":"+port;
    }
    public static String getServiceProvidersPathNode(String domain,String interfaceName,String protocol){
        return serviceBaseNodePath+"/"+domain+"/"+interfaceName+"/"+"providers/"+protocol;
    }

    public static void registService(ServiceStructureInfo serviceStructureInfo)throws Exception{
        String domain = AppDomainNameUtils.getDomainName();
        String group = "default";
        String serviceName = serviceStructureInfo.getServiceId();
        List<Server> servers = ServerManager.enableServers();
        for (Server server:servers){
            String protocol = server.getProtocol();
            String ip = server.getIp();
            int port = server.getPort();
            registService(domain, serviceName, protocol, ip, port, group);
        }
    }
}
