package com.falcon.server.invoke.handle;


import java.lang.reflect.InvocationTargetException;

/**
 * Created by fanshuai on 18/11/28.
 */
public class MethodInvokeLogHandler implements ServiceInvokeHandle{
    @Override
    public void handleInvokeMethod(InvokeServiceMethodContext invokeServiceMethodContext, ServiceInvokeHandleChain invokeHandleChain) throws InvocationTargetException, IllegalAccessException {
        long t1 = System.currentTimeMillis();
        invokeHandleChain.handleInvokeMethod(invokeServiceMethodContext);
        long t2 = System.currentTimeMillis();
        try {
            System.out.println("invoke "+invokeServiceMethodContext.getFalconRequest().getServiceId()+"."+invokeServiceMethodContext.getFalconRequest().getMethodId()+" 用时"+(t2-t1)+"毫秒");
        }catch (Exception e){
            System.out.println("exception");
        }
    }
}
