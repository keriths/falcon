package com.falcon.server.netty;

import com.falcon.server.ServiceProviderManager;
import com.falcon.server.method.ServiceMethod;
import com.falcon.server.servlet.FalconRequest;
import com.falcon.server.servlet.FalconResponse;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by fanshuai on 15-1-23.
 */
public class NettyServerChannelHandler extends SimpleChannelUpstreamHandler{
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

    public void doRequest(final FalconRequest request, final Channel channel){
        //后面修改成线程池
        new Thread(){
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
                        response.setException(new Exception("method not found"));
                    }else{
                        Object o = serviceMethod.invoke(paramters);
                        response.setRetObject(o);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    response.setException(e);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                    response.setException(e);
                } finally {
                    channel.write(response);
                }
            }
        }.start();
    }


    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {

    }
}
