package com.falcon.server.netty;

import com.falcon.client.CustomerManager;
import com.falcon.client.InvokerContext;
import com.falcon.server.servlet.FalconResponse;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import java.util.List;

/**
 * Created by fanshuai on 15-1-24.
 */
public class NettyClientChannelHandler extends SimpleChannelUpstreamHandler {
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        List<FalconResponse> responses =(List<FalconResponse>) e.getMessage();
        if(responses==null || responses.isEmpty()){
            return ;
        }
        for(FalconResponse response:responses){
            long seq = response.getSequence();
            InvokerContext context=CustomerManager.requestIng.get(seq);
            context.getCallBack().processResponse(response);
            CustomerManager.requestIng.remove(seq);
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        e.getCause().printStackTrace();
        System.out.println("------Unexpected exception from downstream."
                + e.getCause());
    }
}
