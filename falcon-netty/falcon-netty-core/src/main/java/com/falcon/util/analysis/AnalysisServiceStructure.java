package com.falcon.util.analysis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.util.Strings;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.*;
import java.util.*;

/**
 * Created by fanshuai on 18/10/27.
 */
public class AnalysisServiceStructure {
    public static Map<String,ServiceStructureInfo> serviceMethodStructureInfoMap = new HashMap<String, ServiceStructureInfo>();

    public static void main(String args[]){
        ServiceStructureInfo serviceStructureInfo = analysisService(new TestService());
        System.out.println(JSON.toJSONString(serviceStructureInfo));
        serviceMethodStructureInfoMap.put(serviceStructureInfo.getServiceTypeName(),serviceStructureInfo);
        List<TestDTO> testDTOList = new ArrayList<TestDTO>();
        testDTOList.add(new TestDTO("1", Lists.newArrayList("1","11","111")));
        testDTOList.add(new TestDTO("2", Lists.newArrayList("2","22","222")));
        Map<String,String> params = new HashMap<String, String>();
        params.put("testDTOList",JSON.toJSONString(testDTOList));
        Object o = process(TestService.class.getName(), "maplist", params);
        System.out.println(o);
    }

    public static Object process(String className,String methodName,Map<String,String> params){
        ServiceStructureInfo serviceStructureInfo = serviceMethodStructureInfoMap.get(className);
        List<ServiceMethodStructureInfo> methodStructureInfos = serviceStructureInfo.getServiceMethodStructureInfos();
        for (ServiceMethodStructureInfo methodStructureInfo : methodStructureInfos){
            if (methodName.equals(methodStructureInfo.getMethodName())){
                Object serviceInstance = methodStructureInfo.getServiceInstance();
                Method method = methodStructureInfo.getMethod();
                LinkedHashMap<String, ParamStructure> paramStructureLinkedHashMap = methodStructureInfo.getParamStructureMap();
                Object[] param = null;
                if (!CollectionUtils.isEmpty(paramStructureLinkedHashMap)){
                    param = new Object[paramStructureLinkedHashMap.size()];
                    int i = 0;
                    for (Map.Entry<String, ParamStructure> entry : paramStructureLinkedHashMap.entrySet()){
                        Object p = null;
                        ParamStructure paramStructure = entry.getValue();
                        String paramName = paramStructure.getParamName();
                        Type type = paramStructure.getParamType();
                        String value = params.get(paramName);
                        if (Strings.isNotBlank(value)){
                            p = JSON.parseObject(value,type, Feature.values());
                        }
                        param[i] = p;
                        i++;
                    }
                }
                try {
                    return method.invoke(serviceInstance,param);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 解析单个服务，返回解析后的服务结构
     * @param serviceInstance
     * @return
     */
    public static ServiceStructureInfo analysisService(Object serviceInstance){
        if (serviceInstance==null){
            return null;
        }
        Class serviceClass =  serviceInstance.getClass();
        ServiceStructureInfo serviceStructureInfo = new ServiceStructureInfo();
        serviceStructureInfo.setServiceInstance(serviceInstance);
        serviceStructureInfo.setServiceTypeName(serviceClass.getName());
        serviceStructureInfo.setServiceDesc("");//TODO 通过文档注解获得
        serviceStructureInfo.setServiceId("");//TODO 通过文档注解获得，没有默认完整类名
        serviceStructureInfo.setServiceMethodStructureInfos(getServiceMethodStructureInfoList(serviceStructureInfo));
        return serviceStructureInfo;
    }
    public static List<ServiceMethodStructureInfo> getServiceMethodStructureInfoList(ServiceStructureInfo serviceStructureInfo){
        List<ServiceMethodStructureInfo> serviceMethodStructureInfos = new ArrayList<ServiceMethodStructureInfo>();
        Method[] methods = serviceStructureInfo.getServiceInstance().getClass().getDeclaredMethods();
        for (Method method:methods){
            //排除方法
            List<String> methodNameList = new ArrayList<String>();
            for (Method objMethod:Object.class.getDeclaredMethods()){
                methodNameList.add(objMethod.getName());
            }
            if (methodNameList.contains(method.getName())){
                return null;
            }
            serviceMethodStructureInfos.add(analysisMethod(serviceStructureInfo,method));
        }
        return serviceMethodStructureInfos;
    }
    public static ServiceMethodStructureInfo analysisMethod(ServiceStructureInfo serviceStructureInfo,Method method){
        //排除方法
        List<String> methodNameList = new ArrayList<String>();
        for (Method objMethod:Object.class.getDeclaredMethods()){
            methodNameList.add(objMethod.getName());
        }
        if (methodNameList.contains(method.getName())){
            return null;
        }
        ServiceMethodStructureInfo serviceMethodStructureInfo = new ServiceMethodStructureInfo();

        serviceMethodStructureInfo.setServiceId(serviceStructureInfo.getServiceId());
        serviceMethodStructureInfo.setServiceTypeName(serviceStructureInfo.getServiceTypeName());
        serviceMethodStructureInfo.setServiceDesc(serviceStructureInfo.getServiceDesc());
        serviceMethodStructureInfo.setServiceInstance(serviceStructureInfo.getServiceInstance());

        serviceMethodStructureInfo.setMethod(method);
        serviceMethodStructureInfo.setMethodName(method.getName());
        serviceMethodStructureInfo.setMethodId("");//TODO 方法id从文档注解中获得
        serviceMethodStructureInfo.setMethodDesc("");//TODO 方法描述从文档注解中获得
        /**
         * 防止循环引用，把已经解析的存放在map中
         */
        Map<String,ClassPropertiesStructure> classPropertiesStructureMap = new HashMap<String, ClassPropertiesStructure>();
        //处理返回
        Type returnType = method.getGenericReturnType();
        serviceMethodStructureInfo.setReturnClassName(returnType.getTypeName());
        //解析返回类型
        analysisClassProperties(returnType,classPropertiesStructureMap);
        //处理参数

        serviceMethodStructureInfo.setParamStructureMap(analysisMethodParamters(method,classPropertiesStructureMap));

        serviceMethodStructureInfo.setClassPropertiesStructureMap(classPropertiesStructureMap);
        return serviceMethodStructureInfo;
    }
    public static LinkedHashMap<String, ParamStructure> analysisMethodParamters(Method method,Map<String,ClassPropertiesStructure> classPropertiesStructureMap){
        LinkedHashMap<String, ParamStructure> paramNameParamStructureMap = new LinkedHashMap<String, ParamStructure>();
        Type[] paramterTypes = method.getGenericParameterTypes();
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = u.getParameterNames(method);
        if (paramterTypes!=null && paramterTypes.length>0){
            for (int i = 0;i<paramterTypes.length;i++){
                Type type = paramterTypes[i];
                String paramName = paramNames[i];
                ParamStructure paramStructure = new ParamStructure();
                paramStructure.setParamName(paramName);
                paramStructure.setParamDesc("");
                paramStructure.setParamTypeName(type.getTypeName());
                paramStructure.setParamType(type);
                paramNameParamStructureMap.put(paramName, paramStructure);
                //解析参数类型
                analysisClassProperties(type,classPropertiesStructureMap);
            }
        }
        return paramNameParamStructureMap;
    }
//    public static ClassPropertiesStructure analysisMethodResult(Method method,Map<String,ClassPropertiesStructure> classPropertiesStructureMap){
//        return analysisClassProperties(method.getGenericReturnType(),classPropertiesStructureMap);
//    }
    public static void analysisClassProperties(Type type,Map<String,ClassPropertiesStructure> classPropertiesStructureMap){
        if (type instanceof ParameterizedType){
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] types = parameterizedType.getActualTypeArguments();
            for (Type thisType : types){
                analysisClassProperties(thisType,classPropertiesStructureMap);
            }
            return ;
        }
        if (type instanceof Class){
            Class thisClass = (Class)type;
            if (thisClass.getName().startsWith("java.")){
                return ;
            }
            ClassPropertiesStructure classPropertiesStructure = classPropertiesStructureMap.get(thisClass.getTypeName());
            if (classPropertiesStructure!=null){
                return ;
            }
            classPropertiesStructure = new ClassPropertiesStructure();
            classPropertiesStructureMap.put(thisClass.getTypeName(),classPropertiesStructure);
            classPropertiesStructure.setType(thisClass);
            classPropertiesStructure.setTypeName(thisClass.getTypeName());

            classPropertiesStructure.setPropertyStructures(getPropertyStructures(thisClass,classPropertiesStructureMap));

            return ;
        }
        throw new RuntimeException("type not class not ");
    }

    public static List<PropertyStructure> getPropertyStructures(Class thisclass,Map<String,ClassPropertiesStructure> classPropertiesStructureMap){
        List<PropertyStructure> propertyStructureList = new ArrayList<PropertyStructure>();


        Field[] fields = thisclass.getDeclaredFields();
        for (Field field:fields){
            PropertyStructure propertyStructure = new PropertyStructure();
            propertyStructure.setPropertyName(field.getName());
            propertyStructure.setPropertyDesc("");//TODO 从注解中取
            propertyStructure.setPropertyType(field.getGenericType().getTypeName());
               //属性类型解析
            analysisClassProperties(field.getType(),classPropertiesStructureMap);
            propertyStructureList.add(propertyStructure);
        }
        return propertyStructureList;
    }

}
