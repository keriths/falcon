package com.falcon.util.analysis;

import com.falcon.config.ProviderConfig;
import com.falcon.server.invoke.handle.DoServiceMethodInvokeHandler;
import com.falcon.server.invoke.handle.InvokeServiceMethodContext;
import com.falcon.server.invoke.handle.ServiceInvokeHandle;
import com.falcon.server.invoke.handle.ServiceInvokeHandleChain;
import com.falcon.server.servlet.FalconRequest;
import com.google.common.collect.Lists;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceManager {
    private static Map<String,ServiceStructureInfo> serviceStructureInfoMap = new HashMap<String, ServiceStructureInfo>();

    /**
     * 解析一个服务
     * @param serviceInstance
     */
    public static void analysisService(String serviceKey,Object serviceInstance){
        ServiceStructureInfo serviceStructureInfo = AnalysisServiceStructure.analysisService(serviceKey,serviceInstance);
        if (serviceStructureInfo==null){
            return;
        }
        if (serviceStructureInfoMap.containsKey(serviceStructureInfo.getServiceId())){
            throw new RuntimeException("********"+serviceStructureInfo.getServiceId()+"******** has analysised");
        }
        serviceStructureInfoMap.put(serviceStructureInfo.getServiceId(),serviceStructureInfo);
    }

    /**
     * 根据服务名，方法名，方法参数获取方法描述
     * @param serviceName
     * @param methodName
     * @param methodParamTypes
     * @return
     */
    public static ServiceMethodStructureInfo getServiceMethodStructureInfo(String serviceName,String methodName,String methodParamTypes) {
        ServiceStructureInfo serviceStructureInfo = serviceStructureInfoMap.get(serviceName);
        if (serviceStructureInfo==null){
            return null;
        }
        return serviceStructureInfo.getServiceMethodStructureInfo(methodName,methodParamTypes);
    }

    /**
     * 根据服务名查询服务描述
     * @param serviceName
     * @return
     */
    public static ServiceStructureInfo getServiceStructureInfo(String serviceName){
        return serviceStructureInfoMap.get(serviceName);
    }

    /**
     * 查询所有的服务名称
     * @return
     */
    public static List<String> getServiceNames(){
        return Lists.newArrayList(serviceStructureInfoMap.keySet().iterator());
    }


    public static void analysisServices(List<ProviderConfig> providerConfigList) {
        if (providerConfigList==null){
            return;
        }
        for (ProviderConfig providerConfig:providerConfigList){
            analysisService(providerConfig.getServiceInterface().getName(),providerConfig.getService());
        }
    }

    public static String getMethodParamTypeString(Class[] paramClasses) {
        if (paramClasses==null || paramClasses.length==0){
            return "()";
        }
        StringBuffer buffer = new StringBuffer("(");
        for (Class c:paramClasses){
            buffer.append(c.getName()).append(",");
        }
        return buffer.substring(0,buffer.length()-1)+")";
    }

    public Object invokeServiceMethod(FalconRequest request,String protocol) throws Exception{
        try {
            String service = request.getServiceInterfaceName();
            String method = request.getServiceMethod();
            String paramTypeNames = request.getParameterTypeNames();
            ServiceMethodStructureInfo methodStructureInfo = ServiceManager.getServiceMethodStructureInfo(service, method, paramTypeNames);
            Assert.notNull(methodStructureInfo, service + "." + method + paramTypeNames + " service not found ");
            ServiceInvokeHandleChain invokeHandleChain = new ServiceInvokeHandleChain();
            invokeHandleChain.addServiceInvokeHandle(new DoServiceMethodInvokeHandler());
            InvokeServiceMethodContext invokeServiceMethodContext = new InvokeServiceMethodContext();
            invokeServiceMethodContext.setMethodStructureInfo(methodStructureInfo);
            invokeServiceMethodContext.setFalconRequest(request);
            invokeServiceMethodContext.setProtocol(protocol);
            invokeHandleChain.handleInvokeMethod(invokeServiceMethodContext);
            return invokeServiceMethodContext.getRetValue();
        } catch (IllegalArgumentException e){
            throw new Exception(e);
        } catch (Exception e){
            throw new Exception(e);
        }
    }

}
