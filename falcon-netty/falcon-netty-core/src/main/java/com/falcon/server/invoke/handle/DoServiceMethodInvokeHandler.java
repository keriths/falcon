package com.falcon.server.invoke.handle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.falcon.util.analysis.ParamStructure;
import com.falcon.util.analysis.ServiceMethodStructureInfo;
import org.apache.logging.log4j.util.Strings;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by fanshuai on 18/11/28.
 */
public class DoServiceMethodInvokeHandler implements ServiceInvokeHandle{
    @Override
    public void handleInvokeMethod(InvokeServiceMethodContext invokeServiceMethodContext, ServiceInvokeHandleChain invokeHandleChain) throws InvocationTargetException, IllegalAccessException {
        ServiceMethodStructureInfo methodStructureInfo = invokeServiceMethodContext.getMethodStructureInfo();
        Object[] params = getParams(invokeServiceMethodContext);
        Object retValue = methodStructureInfo.getMethod().invoke(methodStructureInfo.getServiceInstance(),params);
        invokeServiceMethodContext.setRetValue(retValue);
    }

    private Object[] getParams(InvokeServiceMethodContext invokeServiceMethodContext) {
        if ("http".equals(invokeServiceMethodContext.getProtocol())){
            return getParamObjects(invokeServiceMethodContext.getFalconRequest().getParameters(),invokeServiceMethodContext.getMethodStructureInfo());
        }
        return invokeServiceMethodContext.getFalconRequest().getParameters();
    }
    private Object[] getParamObjects(Object[] paramValues, ServiceMethodStructureInfo methodStructureInfo) {
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

    private Object jsonstrToTypeObject(Type type, String value) {
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
