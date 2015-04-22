package com.falcon.client;

import com.falcon.config.ProviderZKNodeConfig;
import com.falcon.server.servlet.FalconRequest;
import com.falcon.util.FalconAssignTools;
import com.google.common.base.Strings;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by fanshuai on 15-2-11.
 */
public class CustomerManager {
    private static List<CustomerConfig> customerConfigList = new ArrayList<CustomerConfig>();
    private static Map<String,FalconCustomerClient> allClient = new HashMap<String,FalconCustomerClient>();
    private static Map<String,List<FalconCustomerClient>> serviceClientsMap = new HashMap<String, List<FalconCustomerClient>>();


    private static String getServiceClientsMapKey(String domain,String interfaceName){
        return domain+"&"+interfaceName;
    }
    private static String getAllClientKey(String host,int port){
        return host+":"+port;
    }
    public static void addCustomer(CustomerConfig customerConfig) throws Exception {
        customerConfigList.add(customerConfig);
        List<ProviderZKNodeConfig> providerZKNodeConfigList = InvokerZKClientFactory.getClient().getProviders(customerConfig.getDomain(),customerConfig.getServiceInterface().getName());
        if(providerZKNodeConfigList==null || providerZKNodeConfigList.isEmpty()){
            throw new Exception(customerConfig.getDomain()+" "+customerConfig.getServiceInterface()+" 服务提供者没有");
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
                try {
                    FalconCustomerClient client = getClient(customerConfig);
                    InvokerContext invokerContext = new InvokerContext(request,customerConfig,callBack);
                    requestIng.put(request.getSequence(),invokerContext);
                    client.doRequest(request, invokerContext);
                }catch (Exception e){
                    callBack.processFailedResponse(e);
                }
            }
        }.start();
        return callBack.get(customerConfig.getTimeout());
    }
    private static FalconCustomerClient getClient(CustomerConfig customerConfig) throws Exception {
        String key = getServiceClientsMapKey(customerConfig.getDomain(), customerConfig.getServiceInterface().getName());
        FalconCustomerClient client;
        String assignClient = FalconAssignTools.getProperty(key);
        System.out.println(assignClient);
        try {
            if(!StringUtils.isEmpty(assignClient)){
                client = allClient.get(assignClient);
                if(client==null){
                    throw new Exception(assignClient+" client not found ");
                }
            }else{
                List<FalconCustomerClient> clients = serviceClientsMap.get(key);
                client = selectClient(clients);
            }
            return client;
        }catch (Exception e){
            throw new Exception("服务提供者["+key+"] "+e.getMessage(),e);
        }
    }

    private static FalconCustomerClient selectClient(List<FalconCustomerClient> clients) throws Exception {
        if(clients==null || clients.isEmpty()){
            throw new Exception("服务提供者 not fund client");
        }
        int index = randomIndex(clients.size());
        FalconCustomerClient c = clients.get(index);
        if (c.isConnected()){
            return c;
        }
        c.connect();
        if(c.isConnected()){
            return c;
        }
        throw new Exception("服务提供者["+c.getHost()+":"+c.getPort()+"] client is dead");
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
