package com.falcon.client;

import com.falcon.config.ProviderZKNodeConfig;
import com.falcon.server.servlet.FalconRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fanshuai on 15-2-11.
 */
public class CustomerManager {
    private static List<CustomerConfig> customerConfigList = new ArrayList<CustomerConfig>();
    private static Map<String,FalconCustomerClient> allClient = new HashMap<String,FalconCustomerClient>();
    private static Map<String,List<FalconCustomerClient>> serviceClientsMap = new HashMap<String, List<FalconCustomerClient>>();


    private static String getServiceClientsMapKey(String domain,String interfaceName){
        return domain+"_"+interfaceName;
    }
    private static String getAllClientKey(String host,int port){
        return host+":"+port;
    }
    public static void addCustomer(CustomerConfig customerConfig) throws Exception {
        customerConfigList.add(customerConfig);
        List<ProviderZKNodeConfig> providerZKNodeConfigList = InvokerZKClientFactory.getClient().getProviders(customerConfig.getDomain(),customerConfig.getServiceInterface().getName());
        if(providerZKNodeConfigList==null || providerZKNodeConfigList.isEmpty()){
            //TODO没有取到客户諯怎么办呢
            return;
        }

        String erviceClientsMapKey=getServiceClientsMapKey(customerConfig.getDomain(),customerConfig.getServiceInterface().getName());
        List<FalconCustomerClient> serviceClientList = serviceClientsMap.get(customerConfig.getDomain()+"_"+customerConfig.getServiceInterface().getName());
        if(serviceClientList==null){
            serviceClientList = new ArrayList<FalconCustomerClient>();
            serviceClientsMap.put(erviceClientsMapKey,serviceClientList);
        }
        for (ProviderZKNodeConfig providerZKNodeConfig:providerZKNodeConfigList){
            String allClientKey = getAllClientKey(providerZKNodeConfig.getHost(),providerZKNodeConfig.getPort());
            FalconCustomerClient client = allClient.get(allClientKey);
            if(client==null){
                client=new FalconCustomerClient(providerZKNodeConfig.getHost(),providerZKNodeConfig.getPort());
                allClient.put(allClientKey,client);
            }
            serviceClientList.add(client);
        }
    }
    public static Map<Long,InvokerContext> requestIng = new HashMap<Long, InvokerContext>();
    public static Object invoke(final FalconRequest request, final CustomerConfig customerConfig) throws Exception {
        final InvokerCallBack callBack = new InvokerCallBack();
        new Thread(){
            @Override
            public void run() {
                List<FalconCustomerClient> clients = serviceClientsMap.get(getServiceClientsMapKey(customerConfig.getDomain(), customerConfig.getServiceInterface().getName()));
                FalconCustomerClient client = selectClient(clients);
                InvokerContext invokerContext = new InvokerContext(request,customerConfig,callBack);
                requestIng.put(request.getSequence(),invokerContext);
                client.doRequest(request, invokerContext);
            }
        }.start();
        return callBack.get();
    }
    private static FalconCustomerClient selectClient(List<FalconCustomerClient> clients){
        if(clients==null || clients.isEmpty()){
            return null;
        }
        return clients.get(0);
    }
}
