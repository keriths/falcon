package com.falcon.server.netty;

import com.caucho.hessian.io.Hessian2Output;
import com.falcon.config.ProviderConfig;
import com.falcon.server.ServiceProviderManager;
import com.falcon.server.method.ServiceMethod;
import com.falcon.server.servlet.FalconRequest;
import com.falcon.server.servlet.FalconResponse;
import com.falcon.util.analysis.ServiceManager;
import com.falcon.util.analysis.ServiceMethodStructureInfo;
import org.jboss.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by fanshuai on 15-1-23.
 */
public class NettyServerChannelHandler extends SimpleChannelUpstreamHandler{
    private static  final Logger log = LoggerFactory.getLogger(NettyServerChannelHandler.class);
//    ExecutorService threadPool =  Executors.newCachedThreadPool();
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
            throws Exception {
        //log.error("***&&&***",e.get);
        //e.getChannel().write("Hello, World");
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        super.messageReceived(ctx, e);
        List<FalconRequest> requests= (List<FalconRequest>)e.getMessage();
        log.info(requests+" serverHandel receive ");
        for (FalconRequest request:requests){
            doRequest(request, ctx.getChannel());
        }
    }
    ExecutorService nettyServerInvokeThreadPool = new ThreadPoolExecutor(100, 500, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1000),
            new ThreadFactory() {
                AtomicInteger threadNum = new AtomicInteger(0);
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread("falcon-netty-invoke-thread-"+threadNum.incrementAndGet());
                }
            });
    public Future doRequest(final FalconRequest request, final Channel channel){
        //后面修改成线程池
        Thread t = new Thread(){
            @Override
            public void run() {
                FalconResponse response = new FalconResponse();
                response.setSequence(request.getSequence());
                try {
                    Object retObj =  ServiceManager.invokeServiceMethod(request,"tcp");
                    response.setRetObject(retObj);
//
//                    String serviceName = request.getServiceInterfaceName();
//                    String methodName = request.getServiceMethod();
//                    Object[] paramters = request.getParameters();
//                    ServiceMethodStructureInfo serviceMethodStructureInfo = ServiceManager.getServiceMethodStructureInfo(serviceName,methodName,request.getParameterTypeNames());
//                    if (serviceMethodStructureInfo==null){
//                        throw new Exception("");
//                    }
//                    Object retObj = serviceMethodStructureInfo.getMethod().invoke(serviceMethodStructureInfo.getServiceInstance(),paramters);
//                    response.setRetObject(retObj);
                } catch (IllegalAccessException e) {
                    log.error(request + " IllegalAccessException exception :", e);
                    response.setErrorMsg("InvocationTargetException:"+e.getMessage());
                } catch (InvocationTargetException e) {
                    if(e.getTargetException()!=null){
                        log.error(request+" InvocationTargetException exception :",e.getTargetException());
                        response.setErrorMsg("InvocationTargetException:"+e.getTargetException().getMessage());
                    }else {
                        log.error(request+" InvocationTargetException :",e);
                        response.setErrorMsg("InvocationTargetException:"+e.getMessage());
                    }
                } catch (Exception e) {
                    log.error(request+" exception :",e);
                    response.setErrorMsg("Exception:"+e.getMessage());
                } finally {
                    log.info("return response "+response);
                    channel.write(response);
                }
            }
        };
        return nettyServerInvokeThreadPool.submit(t);
    }


    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        log.error("***&&&***",e.getCause());
    }
}
