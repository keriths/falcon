package com.fs.falcon.client;

import com.alibaba.fastjson.JSONObject;
import com.fs.falcon.server.hessian.ProviderConfig;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fanshuai on 15-1-15.
 */
public class ServiceProviders {
    public static Map<String,List<ProviderConfig>> providers = new HashMap<String, List<ProviderConfig>>();
    public static ProviderConfig getServiceProviderConfig(String domain,Class service){
        String key = domain+"_"+service.getName();
        List<ProviderConfig> l = providers.get(key);
        if(CollectionUtils.isEmpty(l)){
            return null;
        }
        return l.get(0);
    }
    public static void addConfigs(String key,List<String> serviceConfigs) {
        System.out.println(key+"【"+serviceConfigs+"】");
        try{
            if(CollectionUtils.isEmpty(serviceConfigs)){
                providers.put(key,null);
                return;
            }
            List<ProviderConfig> configs = new ArrayList<ProviderConfig>();
            for(String config:serviceConfigs){
                ProviderConfig c = (ProviderConfig) JSONObject.parseObject(config, ProviderConfig.class);
                configs.add(c);
            }
            providers.put(key,configs);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
