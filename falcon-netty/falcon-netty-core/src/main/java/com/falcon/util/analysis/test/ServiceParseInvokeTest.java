package com.falcon.util.analysis.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.falcon.util.analysis.AnalysisServiceStructure;
import com.falcon.util.analysis.ParamStructure;
import com.falcon.util.analysis.ServiceMethodStructureInfo;
import com.falcon.util.analysis.ServiceStructureInfo;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.util.Strings;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by fanshuai on 18/10/29.
 */
public class ServiceParseInvokeTest {
    public static Map<String,ServiceStructureInfo> serviceMethodStructureInfoMap = new HashMap<String, ServiceStructureInfo>();

    public static void main(String args[]){
        ServiceStructureInfo serviceStructureInfo = AnalysisServiceStructure.analysisService(new TestService());
        System.out.println(JSON.toJSONString(serviceStructureInfo));
        serviceMethodStructureInfoMap.put(serviceStructureInfo.getServiceTypeName(),serviceStructureInfo);
        List<TestDTO> testDTOList = new ArrayList<TestDTO>();
        testDTOList.add(new TestDTO("1", Lists.newArrayList("1", "11", "111")));
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
}
