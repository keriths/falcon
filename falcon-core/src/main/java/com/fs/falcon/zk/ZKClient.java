package com.fs.falcon.zk;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fanshuai on 15-1-8.
 */
public class ZKClient implements Watcher{
    private ZooKeeper zk ;
    private String host = "192.168.120.130:2181";
    private int sessionTimeout=2000;
    private static ZKClient instance;
    public ZooKeeper getZooKeeper(){
        return zk;
    }
    public static ZKClient getInstance(){
        if(instance==null){
            synchronized (ZKClient.class) {
                if(instance==null){
                    return new ZKClient();
                }
            }
        }
        return instance;
    }
    private ZKClient(){
        try {
            zk = new ZooKeeper(host,sessionTimeout,this);
            while (true){
                if(zk.getState()==ZooKeeper.States.CONNECTED){
                    break;
                }
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String create(String path,byte[] data,CreateMode createMode) throws KeeperException, InterruptedException {
        System.out.println("start -- "+path);
        String p =zk.create(path,data, ZooDefs.Ids.OPEN_ACL_UNSAFE,createMode);
        System.out.println("created --" + p);
        return p;
    }
    Map<String,List<String>> servicesProviders = new HashMap<String, List<String>>();
    String baseServerFolderPath = "/server";
    public void flushServiceProvider() throws KeeperException, InterruptedException {
        if(zk.exists(baseServerFolderPath,true)==null){
            return;
        }
        List<String> serverNodes = zk.getChildren(baseServerFolderPath, true);
        for(String serverNode : serverNodes){
            List<String> providerNodes=zk.getChildren(baseServerFolderPath+"/"+serverNode, true);
            List<String> providerList = new ArrayList<String>();
            for(String providerNode : providerNodes){
                byte[] data = zk.getData(baseServerFolderPath+"/"+serverNode+"/"+providerNode,true,null);
                providerList.add(new String(data));
            }
            servicesProviders.put(serverNode,providerList);
        }
    }
    public String addServer(String serverName) throws KeeperException, InterruptedException {
        if(zk.exists(baseServerFolderPath,true)==null){
            create(baseServerFolderPath, null, CreateMode.PERSISTENT);
        }
        if(zk.exists(baseServerFolderPath+"/"+serverName,true)==null){
            create(baseServerFolderPath+"/"+serverName,null,CreateMode.PERSISTENT);
        }
        return baseServerFolderPath+"/"+serverName;
    }
    public List<String> getServiceProviders(String serviceName,Watcher watcher) throws KeeperException, InterruptedException {
        if(zk.exists(baseServerFolderPath+"/"+serviceName,true)==null){
            return null;
        }
        List<String> providerNodes = zk.getChildren(baseServerFolderPath+"/"+serviceName,watcher);
        if(providerNodes==null || providerNodes.isEmpty()){
            return null;
        }
        List<String> providerConfig = new ArrayList<String>();
        for(String providerNode : providerNodes){
            byte[] config = zk.getData(baseServerFolderPath+"/"+serviceName+"/"+providerNode,watcher,null);
            providerConfig.add(new String(config));
        }
        return providerConfig;
    }
    public String getServiceProvider(String serviceProviderNodePath,Watcher watcher) throws KeeperException, InterruptedException {
        byte[] config = zk.getData(serviceProviderNodePath,watcher,null);
        return new String(config);
    }

    public String addServerProvider(String serverName,String providerInfo) throws KeeperException, InterruptedException {
        if(zk.exists(baseServerFolderPath,true)==null){
            create(baseServerFolderPath,null,CreateMode.PERSISTENT);
        }
        if(zk.exists(baseServerFolderPath+"/"+serverName,true)==null){
            create(baseServerFolderPath+"/"+serverName,null,CreateMode.PERSISTENT);
        }
        return create(baseServerFolderPath+"/"+serverName+"/provider",providerInfo.getBytes(),CreateMode.EPHEMERAL_SEQUENTIAL);
    }
    public void close() throws InterruptedException {
        zk.close();
    }
    public static void main (String[] args){
        ZKClient z = new ZKClient();
        try {
            z.flushServiceProvider();
            Thread.sleep(2000);
            z.addServerProvider("weixin","192.168.0.1:8080_0.01_weixin");
            z.addServerProvider("weixin","192.168.0.1:8080_0.01_weixin");
            int i = 0;
            while (true){
                System.out.println(i+"**********:"+z);
                Thread.sleep(5000);
                if(i>30){
                    break;
                }
                i++;
            }
            z.close();
        }
          catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent e) {
        if(e.getType() == Event.EventType.NodeChildrenChanged){
            System.out.println("-----"+e.getPath()+":NodeChildrenChanged");
        }
        if(e.getType() == Event.EventType.NodeCreated){
            System.out.println("-----"+e.getPath()+":NodeCreated");
        }
        if(e.getType() == Event.EventType.NodeDataChanged){
            System.out.println("-----"+e.getPath()+":NodeDataChanged");
        }
        if(e.getType() == Event.EventType.NodeDeleted){
            System.out.println("-----"+e.getPath()+":NodeDeleted");
        }
        if(e.getType() == Event.EventType.None){
            System.out.println("-----"+e.getPath()+":None");
        }
//        try {
//            flushServiceProvider();
//        } catch (KeeperException ee) {
//            ee.printStackTrace();
//        } catch (InterruptedException ee) {
//            ee.printStackTrace();
//        }
    }

    @Override
    public String toString() {
        return servicesProviders.toString();
    }
}
