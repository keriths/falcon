package com.fs.falcon.client;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

/**
 * Created by fanshuai on 15-1-24.
 */
public class NettyClientChannelHandler extends SimpleChannelUpstreamHandler {
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        Object o = e.getMessage();
        System.out.println(o);
    }

    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        System.out.println("------Unexpected exception from downstream."
                + e.getCause());
    }
}
