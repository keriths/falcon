package com.falcon.util.analysis.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.falcon.util.analysis.AnalysisServiceStructure;
import com.falcon.util.analysis.ParamStructure;
import com.falcon.util.analysis.ServiceMethodStructureInfo;
import com.falcon.util.analysis.ServiceStructureInfo;
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
        BigDecimal ss = new BigDecimal("121.32");
        System.out.println(JSON.toJSONString(ss));
        ServiceStructureInfo serviceStructureInfo = AnalysisServiceStructure.analysisService(new TestService());
        System.out.println(JSON.toJSONString(serviceStructureInfo));
        serviceMethodStructureInfoMap.put(serviceStructureInfo.getServiceTypeName(),serviceStructureInfo);
        serviceMethodStructureInfoMap.put(serviceStructureInfo.getServiceId(),serviceStructureInfo);
        List<TestDTO> testDTOList = new ArrayList<TestDTO>();
        testDTOList.add(new TestDTO("1", Lists.newArrayList("1", "11", "111")));
        testDTOList.add(new TestDTO("2", Lists.newArrayList("2","22","222")));
        Map<String,String> params = new LinkedHashMap<String, String>();
        params.put("testDTOList",JSON.toJSONString(testDTOList));
        params.put("str","\"strrrr\"");
        params.put("i","10");
        params.put("l","222");
        params.put("ll","333");
        params.put("b","12132434");
        Object o = process(TestService.class.getName(), "maplist","(java.util.List,java.lang.String,int,long,java.math.BigDecimal,java.lang.Long)", new ArrayList<String>(params.values()));
        System.out.println(o);
    }

    public static Object process(String serviceName,String methodName,String methodParamTypes,List<String> paramValues){
        ServiceMethodStructureInfo methodStructureInfo = getServiceMethodStructureInfo(serviceName,methodName,methodParamTypes);
        Assert.notNull(methodName,"not found method "+serviceName+"."+methodName+paramValues);
        Object serviceInstance = methodStructureInfo.getServiceInstance();
        Method method = methodStructureInfo.getMethod();
        Object[] param = getParamObjects(paramValues, methodStructureInfo);
        try {
            return method.invoke(serviceInstance,param);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(serviceName+"."+methodName+paramValues+" on invoke IllegalAccessException",e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(serviceName+"."+methodName+paramValues+" on invoke InvocationTargetException",e);
        }

    }
    private static Object[] getParamObjects(List<String> paramValues, ServiceMethodStructureInfo methodStructureInfo) {
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
            String value = paramValues.get(i);
            param[i] = jsonstrToTypeObject(type, value);
            i++;
        }
        return param;
    }

    public static Object process(String className,String methodName,Map<String,String> params){
        ServiceMethodStructureInfo methodStructureInfo = getServiceMethodStructureInfo(className,methodName);
        Assert.notNull(methodName,"not found method "+className+"."+methodName);
        Object serviceInstance = methodStructureInfo.getServiceInstance();
        Method method = methodStructureInfo.getMethod();
        Object[] param = getParamObjects(params, methodStructureInfo);
        try {
            return method.invoke(serviceInstance,param);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(className+"."+methodName+" on invoke IllegalAccessException",e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(className+"."+methodName+" on invoke InvocationTargetException",e);
        }
    }

    private static ServiceMethodStructureInfo getServiceMethodStructureInfo(String serviceName,String methodName,String methodParamTypes) {
        ServiceStructureInfo serviceStructureInfo = serviceMethodStructureInfoMap.get(serviceName);
        List<ServiceMethodStructureInfo> methodStructureInfos = serviceStructureInfo.getServiceMethodStructureInfos();
        for (ServiceMethodStructureInfo methodStructureInfo : methodStructureInfos){
            if (methodName.equals(methodStructureInfo.getMethodName()) && methodParamTypes.equals(methodStructureInfo.getParamTypes())){
                return methodStructureInfo;
            }
        }
        return null;
    }

    private static ServiceMethodStructureInfo getServiceMethodStructureInfo(String className, String methodName) {
        ServiceStructureInfo serviceStructureInfo = serviceMethodStructureInfoMap.get(className);
        List<ServiceMethodStructureInfo> methodStructureInfos = serviceStructureInfo.getServiceMethodStructureInfos();
        for (ServiceMethodStructureInfo methodStructureInfo : methodStructureInfos){
            if (methodName.equals(methodStructureInfo.getMethodName())){
                return methodStructureInfo;
            }
        }
        return null;
    }

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
