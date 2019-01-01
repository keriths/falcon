package com.falcon.client;

import com.falcon.config.ProviderZKNodeConfig;
import com.falcon.server.servlet.FalconRequest;
import com.falcon.util.FalconAssignTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by fanshuai on 15-2-11.
 */
public class CustomerManager {
    private static final Logger log = LoggerFactory.getLogger(CustomerManager.class);
    private static List<CustomerConfig> customerConfigList = new ArrayList<CustomerConfig>();
    private static Map<String,FalconCustomerClient> allClient = new HashMap<String,FalconCustomerClient>();
    private static Map<String,List<FalconCustomerClient>> serviceClientsMap = new ConcurrentHashMap<String, List<FalconCustomerClient>>();


    private static String getServiceClientsMapKey(String domain,String interfaceName,String protocol){
        return domain+"&"+interfaceName+"&"+protocol;
    }
    private static String getAllClientKey(String host,int port){
        return host+":"+port;
    }
    public static void addCustomer(CustomerConfig customerConfig) throws Exception {
        customerConfigList.add(customerConfig);
        initCustomerAllClient(customerConfig.getDomain(),customerConfig.getServiceId(),customerConfig.getProtocol());
    }

    public static void initCustomerAllClient(String domain,String serviceInterfaceName,String protocol) throws Exception{
        List<ProviderZKNodeConfig> providerZKNodeConfigList = InvokerZKClientFactory.getClient().getProviders(domain,serviceInterfaceName,protocol);
        if(providerZKNodeConfigList==null || providerZKNodeConfigList.isEmpty()){
            throw new Exception(domain+" "+serviceInterfaceName+" not found provider");
        }

        String erviceClientsMapKey=getServiceClientsMapKey(domain,serviceInterfaceName,protocol);
        List<FalconCustomerClient> serviceClientList = serviceClientsMap.get(erviceClientsMapKey);
        if(serviceClientList==null){
            serviceClientList = Collections.synchronizedList(new ArrayList<FalconCustomerClient>());
            serviceClientsMap.put(erviceClientsMapKey,serviceClientList);
        }
        for (ProviderZKNodeConfig providerZKNodeConfig:providerZKNodeConfigList){
            String allClientKey = getAllClientKey(providerZKNodeConfig.getHost(),providerZKNodeConfig.getPort());
            FalconCustomerClient client = allClient.get(allClientKey);
            if(client==null){
                client=new FalconCustomerClient(providerZKNodeConfig.getHost(),providerZKNodeConfig.getPort(),protocol);
                allClient.put(allClientKey,client);
            }
            if(!serviceClientList.contains(client)){
                serviceClientList.add(client);
            }
        }
    }
    public static void removeCustomerClient(String domain,String serviceInterfaceName,String protocol,String host,int port) throws Exception{
        String serviceClientsMapKey=getServiceClientsMapKey(domain,serviceInterfaceName,protocol);
        List<FalconCustomerClient> serviceClientList = serviceClientsMap.get(serviceClientsMapKey);
        if(serviceClientList==null){
            return ;
        }
        String allClientKey = getAllClientKey(host,port);
        FalconCustomerClient client = allClient.get(allClientKey);
        if(client==null){
            return;
        }
        allClient.remove(allClientKey);
        serviceClientList.remove(client);
    }
    public static Map<Long,InvokerContext> requestIng = new ConcurrentHashMap<Long, InvokerContext>();
    public static Object invoke(final FalconRequest request, final CustomerConfig customerConfig) throws Exception {
        final InvokerCallBack callBack = new InvokerCallBack();
        callBack.setRequest(request);
        log.info(request + " begin invoke : ");
        new Thread(){
            @Override
            public void run() {
                try {
                    FalconCustomerClient client = getClient(customerConfig);
                    InvokerContext invokerContext = new InvokerContext(request,customerConfig,callBack);
                    requestIng.put(request.getSequence(),invokerContext);
                    client.doRequest(request, invokerContext);
                }catch (Exception e){
                    log.error(request+" invoke exception : ",e);
                    callBack.processFailedResponse(e);
                }
            }
        }.start();
        return callBack.get(customerConfig.getTimeout());
    }

    private static FalconCustomerClient getClient(CustomerConfig customerConfig) throws Exception {
        String key = getServiceClientsMapKey(customerConfig.getDomain(), customerConfig.getServiceId(),customerConfig.getProtocol());
        FalconCustomerClient client;
        String assignClient = FalconAssignTools.getProperty(key);
        System.out.println(assignClient);
        try {
            if(assignClient!=null && assignClient.trim().length()>1){
                client = allClient.get(assignClient);
                if(client==null){
                    throw new Exception(assignClient+" client not found ");
                }
            }else{
                List<FalconCustomerClient> clients = serviceClientsMap.get(key);
                log.info(customerConfig.getCustomerInfo()+" allClients:"+clients);
                List<FalconCustomerClient> selectAllClient = new ArrayList<FalconCustomerClient>(clients);
                client = selectClient(customerConfig,selectAllClient);
                log.info(customerConfig.getCustomerInfo()+" selectedClient:"+client);
            }
            return client;
        }catch (Exception e){
            throw new Exception("serviceId provider ["+key+"] "+e.getMessage(),e);
        }
    }

    private static FalconCustomerClient selectClient(CustomerConfig customerConfig,List<FalconCustomerClient> clients) throws Exception {
        if(clients==null || clients.isEmpty()){
            throw new Exception("serviceId provider not fund client");
        }
        int index = randomIndex(clients.size());
        FalconCustomerClient c = clients.get(index);
        if (!c.isConnected()){
//            c.connect();
        }
        while (!c.isConnected()){
            clients.remove(c);
            if(clients.isEmpty()){
                break;
            }
            c = selectClient(customerConfig,clients);
        }
        if(c.isConnected()){
            return c;
        }else {
            throw new Exception(" no available provier exists for serviceId ["+customerConfig.getCustomerInfo()+"]");
        }
    }
    private static int randomIndex(int size){
        if(size<=1){
            return 0;
        }
        Random r = new Random();
        int a = r.nextInt(size);
        return a%size;
    }
    public static void main(String [] args){
        for(int i = 0;i<100;i++){

        System.out.println(randomIndex(20));
        }
    }
}
