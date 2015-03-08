package com.fs.falcon.client;

import com.fs.falcon.seria.Provider1Decoder;
import com.fs.falcon.seria.Provider1Encoder;
import com.fs.falcon.seria.ProviderDecoder;
import com.fs.falcon.seria.ProviderEncoder;
import com.req.Request;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;

/**
 * Created by fanshuai on 15-1-24.
 */
public class NettyClient {
    private ClientBootstrap bootstrap;
    private String ip = "10.128.120.227";
    private int port = 1119;
    Channel channel;
    public void connect(){
        //创建客户端channel的辅助类,发起connection请求
        ClientBootstrap bootstrap = new ClientBootstrap(
                new NioClientSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));
        //It means one same HelloWorldClientHandler instance is going to handle multiple Channels and consequently the data will be corrupted.
        //基于上面这个描述，必须用到ChannelPipelineFactory每次创建一个pipeline
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() {
                ChannelPipeline pipeline =  Channels.pipeline();
                pipeline.addLast("decoder", new ProviderDecoder());
                pipeline.addLast("encoder", new ProviderEncoder());
                //pipeline.addLast("decoder", new ObjectDecoder());
                //pipeline.addLast("encoder", new ObjectEncoder());
                pipeline.addLast("handler", new NettyClientChannelHandler());
                return pipeline;
            }
        });
        bootstrap.setOption("tcpNoDelay", true);
        bootstrap.setOption("keepAlive", true);
        bootstrap.setOption("reuseAddress", true);
        //创建无连接传输channel的辅助类(UDP),包括client和server
        String ip = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        InetSocketAddress address = new InetSocketAddress(ip,port);
       // InetSocketAddress i = new InetSocketAddress( port);
        ChannelFuture future = bootstrap.connect(address);
        channel = future.getChannel();

    }

    public void sendMsg(final String msg){
        Request r = new Request(msg);
       // ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
        ChannelFuture f = channel.write(msg);
        f.addListener(new ChannelFutureListener(){

            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                System.out.println(msg+future.isSuccess());
            }
        });
    }
    public void close(){
        channel.close();
        bootstrap.releaseExternalResources();
    }
}
