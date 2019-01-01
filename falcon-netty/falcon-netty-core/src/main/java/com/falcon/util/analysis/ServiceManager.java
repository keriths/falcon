package com.falcon.util.analysis;

import com.falcon.config.ProviderConfig;
import com.falcon.server.invoke.handle.*;
import com.falcon.server.servlet.FalconRequest;
import com.google.common.collect.Lists;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceManager {
    private static Map<String,ServiceStructureInfo> serviceStructureInfoMap = new HashMap<String, ServiceStructureInfo>();

    public static Map<String,ServiceStructureInfo> getServiceStructureInfoMap(){
        return serviceStructureInfoMap;
    }
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
    @Deprecated
    public static ServiceMethodStructureInfo getServiceMethodStructureInfo(String serviceName,String methodName,String methodParamTypes) {
        ServiceStructureInfo serviceStructureInfo = serviceStructureInfoMap.get(serviceName);
        if (serviceStructureInfo==null){
            return null;
        }
        return serviceStructureInfo.getServiceMethodStructureInfo(methodName,methodParamTypes);
    }

    public static ServiceMethodStructureInfo getServiceMethodStructureInfo(String serviceName,String methodId) {
        ServiceStructureInfo serviceStructureInfo = serviceStructureInfoMap.get(serviceName);
        if (serviceStructureInfo==null){
            return null;
        }
        return serviceStructureInfo.getServiceMethodStructureInfo(methodId);
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
            analysisService(providerConfig.getServiceId(),providerConfig.getService());
        }
    }

    public static String getFullMethodName(Method method){
        return method.getName()+getMethodParamTypeString(method.getParameterTypes());
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

    public static Object invokeServiceMethod(FalconRequest request,String protocol) throws Exception{
        try {
            String service = request.getServiceId();
            String methodId = request.getMethodId();
//            String paramTypeNames = request.getParameterTypeNames();
            ServiceMethodStructureInfo methodStructureInfo = ServiceManager.getServiceMethodStructureInfo(service, methodId);
            Assert.notNull(methodStructureInfo, service + "." + methodId + " serviceId not found ");
            ServiceInvokeHandleChain invokeHandleChain = new ServiceInvokeHandleChain();
            invokeHandleChain.addServiceInvokeHandle(new MethodInvokeLogHandler());
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
