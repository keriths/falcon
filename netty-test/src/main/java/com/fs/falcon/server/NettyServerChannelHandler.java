package com.fs.falcon.server;

import org.jboss.netty.channel.*;

/**
 * Created by fanshuai on 15-1-23.
 */
public class NettyServerChannelHandler extends SimpleChannelUpstreamHandler{
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
            throws Exception {
        //System.out.println("channelConnected");
        //e.getChannel().write("Hello, World");
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        Object o=e.getMessage();
        System.out.println(o);
        //e.getChannel().write("start hello "+e.getMessage()+" threadid:"+Thread.currentThread().getName());
        //Thread.sleep(2000);
        //e.getChannel().write(o);
    }

    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        System.out.println("Unexpected exception from downstream."
                + e.getCause());
        e.getChannel().close();
    }
}
