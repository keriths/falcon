package com.falcon.server.invoke.handle;

import com.falcon.util.analysis.ServiceMethodStructureInfo;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanshuai on 18/11/28.
 */
public class ServiceInvokeHandleChain{
    int index = -1;
    List<ServiceInvokeHandle> serviceInvokeHandleList = new ArrayList<ServiceInvokeHandle>();
    Boolean hasNext(){
        return serviceInvokeHandleList.size()>index+1;
    }
    ServiceInvokeHandle next(){
        return serviceInvokeHandleList.get(++index);
    }
    public void addServiceInvokeHandle(ServiceInvokeHandle serviceInvokeHandle){
        serviceInvokeHandleList.add(serviceInvokeHandle);
    }
    public void handleInvokeMethod(InvokeServiceMethodContext invokeServiceMethodContext) throws InvocationTargetException, IllegalAccessException {
        if (hasNext()){
            next().handleInvokeMethod(invokeServiceMethodContext,this);
        }
    }
}
