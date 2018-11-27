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
public class MethodInvokeLogHandler implements ServiceInvokeHandle{
    @Override
    public void handleInvokeMethod(InvokeServiceMethodContext invokeServiceMethodContext, ServiceInvokeHandleChain invokeHandleChain) throws InvocationTargetException, IllegalAccessException {
        try {
            System.out.println("begin");
            invokeHandleChain.handleInvokeMethod(invokeServiceMethodContext);
            System.out.println("end");
        }catch (Exception e){
            System.out.println("exception");
        }
    }
}
