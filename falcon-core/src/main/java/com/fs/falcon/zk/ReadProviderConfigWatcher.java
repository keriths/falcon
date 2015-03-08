package com.fs.falcon.zk;

import com.fs.falcon.client.ServiceProviders;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.List;

/**
 * Created by fanshuai on 15-1-15.
 */
public class ReadProviderConfigWatcher implements Watcher {
    @Override
    public void process(WatchedEvent watchedEvent) {
        try {
            ZKClient z = ZKClient.getInstance();
            if(watchedEvent.getType() == Event.EventType.NodeDataChanged){
                String path []=watchedEvent.getPath().split("/");
                String serviceName = path[path.length-2];
                List<String> serviceConfigs = z.getServiceProviders(serviceName, this);
                ServiceProviders.addConfigs(serviceName,serviceConfigs);
            }
            if(watchedEvent.getType() == Event.EventType.NodeCreated){

            }
            if(watchedEvent.getType() == Event.EventType.NodeDeleted){

            }
            if(watchedEvent.getType() == Event.EventType.NodeChildrenChanged){
                String path []=watchedEvent.getPath().split("/");
                String serviceName = path[path.length-1];
                List<String> serviceConfigs = z.getServiceProviders(serviceName,this);

                ServiceProviders.addConfigs(serviceName, serviceConfigs);
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
