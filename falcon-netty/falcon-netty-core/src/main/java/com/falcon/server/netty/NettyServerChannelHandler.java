package com.falcon.server.netty;

import com.caucho.hessian.io.Hessian2Output;
import com.falcon.config.ProviderConfig;
import com.falcon.server.ServiceProviderManager;
import com.falcon.server.method.ServiceMethod;
import com.falcon.server.servlet.FalconRequest;
import com.falcon.server.servlet.FalconResponse;
import org.apache.log4j.Logger;
import org.jboss.netty.channel.*;

import java.io.FileOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by fanshuai on 15-1-23.
 */
public class NettyServerChannelHandler extends SimpleChannelUpstreamHandler{
    private static  final Logger log = Logger.getLogger(NettyServerChannelHandler.class);
    ExecutorService threadPool =  Executors.newCachedThreadPool();
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
            throws Exception {
        //e.getChannel().write("Hello, World");
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        super.messageReceived(ctx, e);
        List<FalconRequest> requests= (List<FalconRequest>)e.getMessage();
        for (FalconRequest request:requests){
            log.info(request.getRequestInfo()+" provider receive ");
            doRequest(request, ctx.getChannel());
        }
    }

    public Future doRequest(final FalconRequest request, final Channel channel){
        //后面修改成线程池
        Thread t = new Thread(){
            @Override
            public void run() {
                FalconResponse response = new FalconResponse();
                response.setSequence(request.getSequence());
                try {
                    String serviceName = request.getServiceInterfaceName();
                    String methodName = request.getServiceMethod();
                    Object[] paramters = request.getParameters();
                    Class[] paramTypes = request.getParameterTypes();
                    if(methodName.equals("toString")){
                        ProviderConfig provider = ServiceProviderManager.getProvider(serviceName);
                        if (provider==null){
                            throw new Exception(" provider service("+serviceName+") not found ");
                        }
                        response.setRetObject(provider.getService().toString());
                    }else if(methodName.equals("hashCode")){
                        ProviderConfig provider = ServiceProviderManager.getProvider(serviceName);
                        if (provider==null){
                            throw new Exception(" provider service("+serviceName+") not found ");
                        }
                        response.setRetObject(provider.getService().hashCode());
                    }
                    else {
                        ServiceMethod serviceMethod = ServiceProviderManager.getServiceMethod(serviceName, methodName, ServiceMethod.getParamNameString(paramTypes));
                        if (serviceMethod == null) {
                            response.setErrorMsg(" method not found ");
                        } else {
                            Object o = serviceMethod.invoke(paramters);
                            log.info(request.getRequestInfo()+" has complete ");
                            if (o != null && !(o instanceof Serializable)) {
                                throw new Exception(o.getClass().getName() + " no serializable ");
                            } else {
                                response.setRetObject(o);
                            }
                        }
                    }
                } catch (IllegalAccessException e) {
                    log.error(request.getRequestInfo() + " IllegalAccessException exception :", e);
                    response.setErrorMsg(e.getMessage());
                } catch (InvocationTargetException e) {
                    if(e.getTargetException()!=null){
                        log.error(request.getRequestInfo()+" InvocationTargetException exception :",e.getTargetException());
                        response.setErrorMsg(e.getTargetException().getMessage());
                    }else {
                        log.error(request.getRequestInfo()+" exception :",e);
                        response.setErrorMsg(e.getMessage());
                    }
                } catch (Exception e) {
                    log.error(request.getRequestInfo()+" exception :",e);
                    response.setErrorMsg(e.getMessage());
                } finally {
                    channel.write(response);
                }
            }
        };
        return threadPool.submit(t);
    }


    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {

    }
}
