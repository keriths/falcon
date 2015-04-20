package com.falcon.regist;

import com.falcon.client.ZKManager;
import com.falcon.server.ServiceProviderManager;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.listen.Listenable;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.Executor;

/**
 * Created by fanshuai on 15-2-9.
 */
public class ZKClient {
    private CuratorFramework curatorFramework ;
    private String zkAddress = ZKManager.getZKAddress();
    public ZKClient() throws Exception{
        curatorFramework = CuratorFrameworkFactory.newClient(zkAddress,new ExponentialBackoffRetry(1000, 3));
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
    public long getSessionId()throws Exception{
        return curatorFramework.getZookeeperClient().getZooKeeper().getSessionId();
    }
    public boolean isConnected(){
        return curatorFramework.getZookeeperClient().isConnected();
    }

    public void createNodeWithEPHEMERAL(String path,byte[] data) throws Exception {
        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, data);
    }
    public void deleteNode(String path)throws Exception{
        curatorFramework.delete().forPath(path);
    }

    public boolean existsNode(String pathNode) throws Exception {
        Stat stat = curatorFramework.checkExists().forPath(pathNode);
        if(stat==null){
            return false;
        }
        return true;
    }
    public Stat nodeStat(String pathNode) throws Exception {
        Stat stat = curatorFramework.checkExists().forPath(pathNode);
        return stat;
    }

    class MyListenner implements CuratorListener {
        @Override
        public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {

        }
    }

    class MyConnectionStateListener implements ConnectionStateListener {
        @Override
        public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
            if (connectionState == ConnectionState.LOST) {
                while (true) {
                    try {
                        if (curatorFramework.getZookeeperClient().blockUntilConnectedOrTimedOut()) {
                            ServiceProviderManager.registService();
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
                //重新连接上.重新注册服务
                try {
                    ServiceProviderManager.registService();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
