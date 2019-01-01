package com.falcon.util.analysis;

import com.falcon.server.ServerManager;
import com.falcon.util.analysis.annotation.DESC;
import com.falcon.util.analysis.annotation.MethodID;
import com.falcon.util.analysis.annotation.ServiceID;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

/**
 * Created by fanshuai on 18/10/27.
 */
public class AnalysisServiceStructure {
    /**
     * 解析单个服务，返回解析后的服务结构
     * @param serviceInstance
     * @return
     */
    public static ServiceStructureInfo analysisService(Object serviceInstance){
        return analysisService(null,serviceInstance);
    }

    public static ServiceStructureInfo analysisService(String serviceKey,Object serviceInstance){
        if (serviceInstance==null){
            return null;
        }
        ServiceStructureInfo serviceStructureInfo = new ServiceStructureInfo();
        Class serviceClass =  serviceInstance.getClass();

        serviceStructureInfo.setServiceId(getServiceKey(serviceKey, serviceClass));
        serviceStructureInfo.setServiceInstance(serviceInstance);
        serviceStructureInfo.setServiceTypeName(serviceClass.getName());
        serviceStructureInfo.setServiceDesc(getDesc(serviceClass.getAnnotations()));
        serviceStructureInfo.setServiceMethodStructureInfos(getServiceMethodStructureInfoList(serviceStructureInfo));

        return serviceStructureInfo;
    }

    /**
     * 从注解中获得描述
     * @return
     */
    public static String getDesc(Annotation[] annotations){
        Annotation annotation = getAnnotation(annotations,DESC.class);
        if (annotation==null){
            return null;
        }
        return ((DESC)annotation).value();
    }
    public static Annotation getAnnotation(Annotation[] annotations,Class annotationClass){
        if (annotations==null || annotations.length==0){
            return null;
        }
        for (Annotation annotation : annotations){
            if (annotation.annotationType()==annotationClass){
                return annotation;
            }
        }
        return null;
    }
    public static String getServiceKey(String serviceKey,Class serviceClass){
        if (serviceKey!=null && serviceKey.length()>0){
            return serviceKey;
        }
        Annotation annotation = getAnnotation(serviceClass.getAnnotations(),ServiceID.class);
        if (annotation==null){
            return serviceClass.getName();
        }
        return ((ServiceID)annotation).value();
    }
    public static String getServiceId(Annotation[] annotations){
        Annotation annotation = getAnnotation(annotations,ServiceID.class);
        if (annotation==null){
            return null;
        }
        return ((ServiceID)annotation).value();
    }
    public static String getMethodId(Annotation[] annotations){
        Annotation annotation = getAnnotation(annotations,MethodID.class);
        if (annotation==null){
            return null;
        }
        return ((MethodID)annotation).value();
    }



    private static List<String> excludeMethodNameList = new ArrayList<String>();
    static {
        for (Method objMethod:Object.class.getDeclaredMethods()){
            excludeMethodNameList.add(objMethod.getName());
        }
    }
    public static List<ServiceMethodStructureInfo> getServiceMethodStructureInfoList(ServiceStructureInfo serviceStructureInfo){
        List<ServiceMethodStructureInfo> serviceMethodStructureInfos = new ArrayList<ServiceMethodStructureInfo>();
        Method[] methods = serviceStructureInfo.getServiceInstance().getClass().getMethods();
        for (Method method:methods){
            //排除方法
            if (excludeMethodNameList.contains(method.getName())){
                continue;
            }
            serviceMethodStructureInfos.add(analysisMethod(serviceStructureInfo,method));
        }
        return serviceMethodStructureInfos;
    }
    public static ServiceMethodStructureInfo analysisMethod(ServiceStructureInfo serviceStructureInfo,Method method){
        //排除方法
        ServiceMethodStructureInfo serviceMethodStructureInfo = new ServiceMethodStructureInfo();

        serviceMethodStructureInfo.setServiceId(serviceStructureInfo.getServiceId());
        serviceMethodStructureInfo.setServiceTypeName(serviceStructureInfo.getServiceTypeName());
        serviceMethodStructureInfo.setServiceDesc(serviceStructureInfo.getServiceDesc());
        serviceMethodStructureInfo.setServiceInstance(serviceStructureInfo.getServiceInstance());

        serviceMethodStructureInfo.setMethod(method);
        serviceMethodStructureInfo.setMethodName(method.getName());
//        serviceMethodStructureInfo.setParamTypes(ServiceManager.getMethodParamTypeString(method.getParameterTypes()));
        serviceMethodStructureInfo.setMethodId(ServiceManager.getFullMethodName(method));
        serviceMethodStructureInfo.setMethodDesc(getDesc(method.getAnnotations()));

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
            propertyStructure.setPropertyDesc(getDesc(field.getAnnotations()));
            propertyStructure.setPropertyType(field.getGenericType().getTypeName());
               //属性类型解析
            analysisClassProperties(field.getType(),classPropertiesStructureMap);
            propertyStructureList.add(propertyStructure);
        }
        return propertyStructureList;
    }

}
