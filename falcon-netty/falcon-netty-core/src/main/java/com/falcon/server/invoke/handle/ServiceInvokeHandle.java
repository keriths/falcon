package com.falcon.server.invoke.handle;

import com.falcon.util.analysis.ServiceMethodStructureInfo;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by fanshuai on 18/11/28.
 */
public interface ServiceInvokeHandle {
    void handleInvokeMethod(InvokeServiceMethodContext invokeServiceMethodContext,ServiceInvokeHandleChain invokeHandleChain) throws InvocationTargetException, IllegalAccessException;
}
