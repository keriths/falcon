package com.falcon.client;

import com.alibaba.fastjson.JSON;
import com.falcon.config.ProviderZKNodeConfig;
import com.falcon.regist.ServiceRegistManager;
import com.falcon.server.ServiceProviderManager;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorEventType;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanshuai on 15-2-11.
 */
public class InvokerZKClient {
    private final static Logger log = LoggerFactory.getLogger(InvokerZKClient.class);
    private CuratorFramework curatorFramework ;
    private String zkAddress = ZKManager.getZKAddress();
    public InvokerZKClient() throws Exception{
        curatorFramework = CuratorFrameworkFactory.newClient(zkAddress, new ExponentialBackoffRetry(1000, 3));
        curatorFramework.getConnectionStateListenable().addListener(new MyConnectionStateListener());
        curatorFramework.getCuratorListenable().addListener(new MyListenner());
        curatorFramework.start();
        boolean hasConnected = curatorFramework.getZookeeperClient().blockUntilConnectedOrTimedOut();
        if (!hasConnected){
            throw new RuntimeException("zkServer("+zkAddress+") cont connected ");
        }
        if(!curatorFramework.getZookeeperClient().isConnected()){
            throw new RuntimeException("zkServer("+zkAddress+") has not connected ");
        }
    }

    public List<ProviderZKNodeConfig> getProviders(String domain,String interfaceName,String protocol) throws Exception {
        String parentNode = ServiceRegistManager.getServiceProvidersPathNode(domain,interfaceName,protocol);
        List<String> childrenNodes = curatorFramework.getChildren().watched().forPath(parentNode);
        if(childrenNodes==null || childrenNodes.isEmpty()){
            return null;
        }
        List<ProviderZKNodeConfig> l = new ArrayList<ProviderZKNodeConfig>();
        for(String childNode:childrenNodes){
            byte[] data = curatorFramework.getData().watched().forPath(parentNode+"/"+childNode);
            String dataValue = new String(data);
            ProviderZKNodeConfig providerZKNodeConfig = JSON.parseObject(dataValue,ProviderZKNodeConfig.class);
            l.add(providerZKNodeConfig);
        }
        return l;
    }


    class MyListenner implements CuratorListener {
        @Override
        public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
            //if (event.getType() == CuratorEventType.)
            if(event.getWatchedEvent().getType().getIntValue()== Watcher.Event.EventType.NodeChildrenChanged.getIntValue()){
                try{
                    String parentPath = event.getPath();
                    ///falcon/Server/falcon_netty_demo_provider/com.falcon.demo.api.HelloWordService/providers
                    int startPos = parentPath.indexOf("/falcon/Server/")+"/falcon/Server/".length();
                    int endPos = parentPath.indexOf("/providers");
                    String[] serviceNames = parentPath.substring(startPos,endPos).split("/");
                    String protocol = parentPath.substring(endPos + 11).split("/")[0];
                    String domain = serviceNames[0];
                    String serviceName=serviceNames[1];
                    CustomerManager.initCustomerAllClient(domain,serviceName,protocol);
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }
            }
            else if(event.getWatchedEvent().getType().getIntValue()== Watcher.Event.EventType.NodeDeleted.getIntValue()){
                try {
                    String parentPath = event.getPath();
                    ///falcon/Server/falcon_netty_demo_provider/com.falcon.demo.api.HelloWordService/providers
                    int startPos = parentPath.indexOf("/falcon/Server/")+"/falcon/Server/".length();
                    int endPos = parentPath.indexOf("/providers");
                    String protocol = parentPath.substring(endPos + 11).split("/")[0];
                    String hostAndPort = parentPath.substring(endPos + 11).split("/")[1];
                    String[] providerHosts = hostAndPort.split(":");
                    String[] serviceNames = parentPath.substring(startPos,endPos).split("/");
                    String domain = serviceNames[0];
                    String serviceName=serviceNames[1];
                    CustomerManager.removeCustomerClient(domain,serviceName,protocol,providerHosts[0],Integer.decode(providerHosts[1]));
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }

            }
        }
    }

    class MyConnectionStateListener implements ConnectionStateListener {
        @Override
        public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
            if (connectionState == ConnectionState.LOST) {
                while (true) {
                    try {
                        if (curatorFramework.getZookeeperClient().blockUntilConnectedOrTimedOut()) {
                            break;
                        }
                    } catch (InterruptedException e) {
                        //TODO: log something
                        break;
                    } catch (Exception e) {
                        //TODO: log something
                    }
                }
            }else if(connectionState == ConnectionState.RECONNECTED){
                //重新取客户端链接
                try {
                    //ServiceProviderManager.registService();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
