package com.falcon.server.netty;

import com.caucho.hessian.io.Hessian2Output;
import com.falcon.server.ServiceProviderManager;
import com.falcon.server.method.ServiceMethod;
import com.falcon.server.servlet.FalconRequest;
import com.falcon.server.servlet.FalconResponse;
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
            doRequest(request,ctx.getChannel());
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
                    ServiceMethod serviceMethod = ServiceProviderManager.getServiceMethod(serviceName,methodName,ServiceMethod.getParamNameString(paramTypes));
                    if(serviceMethod==null){
                        response.setThrowable(new Exception("method not found"));
                    }else{
                        Object o = serviceMethod.invoke(paramters);
                        if(o!=null && !(o instanceof Serializable)){
                            throw new Exception(o.getClass().getName() +" no serializable ");
                        }else{
                            response.setRetObject(o);
//                            FileOutputStream ooo = new FileOutputStream("/data/env/aaa");
//                            ooo.write(new byte[4]);
//                            Hessian2Output h2out = new Hessian2Output(ooo);
//                            try {
//                                //h2out.writeObject(o);
//                                response.setRetObject(o);
//                                //response.setRetObject("1111111111111111111111111111");
//                                h2out.writeObject(response);
//                                h2out.flush();
//                               // response.setRetObject(o);
//                            }catch (Exception e){
//                                h2out.init(ooo);
//                                response.setRetObject(null);
//                                //h2out.flush();
//                                h2out.writeObject(response);
//                                h2out.flush();
//                                //response.setThrowable(e);
//                            }finally {
//                                h2out.close();
//                            }
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    response.setErrorMsg(e.getMessage());
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                    if(e.getTargetException()!=null){
                        response.setErrorMsg(e.getTargetException().getMessage());
                    }else {
                        response.setErrorMsg(e.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
