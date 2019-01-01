package com.falcon.util.analysis.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.falcon.server.servlet.FalconRequest;
import com.falcon.util.analysis.*;
import com.falcon.util.analysis.jetty.JettyServer;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.util.Strings;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by fanshuai on 18/10/29.
 */
public class ServiceParseInvokeTest {
    public static Map<String,ServiceStructureInfo> serviceMethodStructureInfoMap = new HashMap<String, ServiceStructureInfo>();

    public static void main(String args[]){
        System.out.println(JSON.toJSONString(new Date()));
        BigDecimal ss = new BigDecimal("121.32");
        System.out.println(JSON.toJSONString(ss));
        ServiceStructureInfo serviceStructureInfo = AnalysisServiceStructure.analysisService(new TestService());
        System.out.println(JSON.toJSONString(serviceStructureInfo));
        serviceMethodStructureInfoMap.put(serviceStructureInfo.getServiceTypeName(),serviceStructureInfo);
        serviceMethodStructureInfoMap.put(serviceStructureInfo.getServiceId(),serviceStructureInfo);
        JettyServer jettyServer = new JettyServer(8888,"/");
        try {
            jettyServer.start();
        }catch (Exception e){
            e.printStackTrace();
        }
        List<TestDTO> testDTOList = new ArrayList<TestDTO>();
        testDTOList.add(new TestDTO("1", Lists.newArrayList("1", "11", "111")));
        testDTOList.add(new TestDTO("2", Lists.newArrayList("2","22","222")));
        Map<String,String> params = new LinkedHashMap<String, String>();
        params.put("testDTOList",JSON.toJSONString(testDTOList));
        params.put("str","strrrr");
        params.put("i","10");
        params.put("l","222");
        params.put("ll","333");
        params.put("b","12132434");
        params.put("date","1542728084138");
//        Object o = process(TestService.class.getName(), "maplist","(java.util.List,java.lang.String,int,long,java.math.BigDecimal,java.lang.Long,java.util.Date)", new ArrayList<String>(params.values()));
//        System.out.println(o);
        System.out.println("");
        System.out.println("");
        System.out.println("");
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setServiceAppKey("TestDomain");
        requestDTO.setServiceName(TestService.class.getName());
        requestDTO.setMethodName("maplist");
//        requestDTO.setParamTypeNames("(java.util.List,java.lang.String,int,long,java.math.BigDecimal,java.lang.Long,java.util.Date)");
        requestDTO.setMethodId("maplist(java.util.List,java.lang.String,int,long,java.math.BigDecimal,java.lang.Long,java.util.Date)");
        requestDTO.setParamValues(new Object[]{"[{\"name\":\"nameisshuai\",\"aaa\":[\"1\"]}]","stringisaaa","111","222","333.333","3434","1542728084138"});
        Object obj = process(requestDTO);
        System.out.println(JSON.toJSONString(obj));
        try {
            Thread.sleep(1000*60*60*24);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Object process(RequestDTO requestDTO){
        Object o = process(requestDTO.getServiceName(),requestDTO.getMethodId(),requestDTO.getParamValues());
        return o;
    }
    public static Object process(FalconRequest falconRequest){
        Object o = process(falconRequest.getServiceId(),falconRequest.getMethodId(),falconRequest.getParameters());
        return o;
    }

    public static Object process(String serviceName,String methodId,Object[] paramValues){
        ServiceMethodStructureInfo methodStructureInfo = getServiceMethodStructureInfo(serviceName,methodId);
        Assert.notNull(methodStructureInfo,"not found method "+serviceName+"."+methodId);
        Object serviceInstance = methodStructureInfo.getServiceInstance();
        Method method = methodStructureInfo.getMethod();
        Object[] param = getParamObjects(paramValues, methodStructureInfo);
        try {
            return method.invoke(serviceInstance,param);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(serviceName+"."+methodId+" on invoke IllegalAccessException",e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(serviceName+"."+methodId+" on invoke InvocationTargetException",e);
        }

    }
    private static Object[] getParamObjects(Object[] paramValues, ServiceMethodStructureInfo methodStructureInfo) {
        LinkedHashMap<String, ParamStructure> paramStructureLinkedHashMap = methodStructureInfo.getParamStructureMap();
        if (CollectionUtils.isEmpty(paramStructureLinkedHashMap)){
            return null;
        }
        Object[] param = new Object[paramStructureLinkedHashMap.size()];
        int i = 0;
        for (Map.Entry<String, ParamStructure> entry : paramStructureLinkedHashMap.entrySet()){
            ParamStructure paramStructure = entry.getValue();
            String paramName = paramStructure.getParamName();
            Type type = paramStructure.getParamType();
            Object value = paramValues[i];
            param[i] = jsonstrToTypeObject(type, (String)value);
            i++;
        }
        return param;
    }

//    public static Object process(String className,String methodName,Map<String,String> params){
//        ServiceMethodStructureInfo methodStructureInfo = getServiceMethodStructureInfo(className,methodName);
//        Assert.notNull(methodName,"not found method "+className+"."+methodName);
//        Object serviceInstance = methodStructureInfo.getServiceInstance();
//        Method method = methodStructureInfo.getMethod();
//        Object[] param = getParamObjects(params, methodStructureInfo);
//        try {
//            return method.invoke(serviceInstance,param);
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException(className+"."+methodName+" on invoke IllegalAccessException",e);
//        } catch (InvocationTargetException e) {
//            throw new RuntimeException(className+"."+methodName+" on invoke InvocationTargetException",e);
//        }
//    }

    private static ServiceMethodStructureInfo getServiceMethodStructureInfo(String serviceName,String methodId) {
        ServiceStructureInfo serviceStructureInfo = serviceMethodStructureInfoMap.get(serviceName);
        List<ServiceMethodStructureInfo> methodStructureInfos = serviceStructureInfo.getServiceMethodStructureInfos();
        for (ServiceMethodStructureInfo methodStructureInfo : methodStructureInfos){
            if (methodStructureInfo.getMethodId().equals(methodId)){
                return methodStructureInfo;
            }
        }
        return null;
    }

//    private static ServiceMethodStructureInfo getServiceMethodStructureInfo(String className, String methodName) {
//        ServiceStructureInfo serviceStructureInfo = serviceMethodStructureInfoMap.get(className);
//        List<ServiceMethodStructureInfo> methodStructureInfos = serviceStructureInfo.getServiceMethodStructureInfos();
//        for (ServiceMethodStructureInfo methodStructureInfo : methodStructureInfos){
//            if (methodName.equals(methodStructureInfo.getMethodName())){
//                return methodStructureInfo;
//            }
//        }
//        return null;
//    }

    private static Object[] getParamObjects(Map<String, String> params, ServiceMethodStructureInfo methodStructureInfo) {
        LinkedHashMap<String, ParamStructure> paramStructureLinkedHashMap = methodStructureInfo.getParamStructureMap();
        if (CollectionUtils.isEmpty(paramStructureLinkedHashMap)){
            return null;
        }
        Object[] param = new Object[paramStructureLinkedHashMap.size()];
        int i = 0;
        for (Map.Entry<String, ParamStructure> entry : paramStructureLinkedHashMap.entrySet()){
            ParamStructure paramStructure = entry.getValue();
            String paramName = paramStructure.getParamName();
            Type type = paramStructure.getParamType();
            String value = params.get(paramName);
            param[i] = jsonstrToTypeObject(type, value);
            i++;
        }
        return param;
    }

    private static Object jsonstrToTypeObject(Type type, String value) {
        Object p = null;
        if (Strings.isNotBlank(value)){
            if (type instanceof Class && ((Class) type).getName().equals(String.class.getName())){
                return value;
            }
            p = JSON.parseObject(value, type, Feature.values());
        }
        return p;
    }
}
