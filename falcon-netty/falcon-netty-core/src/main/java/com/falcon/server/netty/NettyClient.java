package com.falcon.server.netty;

import com.falcon.client.CustomerManager;
import com.falcon.client.InvokerCallBack;
import com.falcon.client.InvokerContext;
import com.falcon.server.servlet.FalconRequest;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;

/**
 * Created by fanshuai on 15-2-10.
 */
public class NettyClient  {
    private ClientBootstrap bootstrap;
    private String ip = "10.128.120.227";
    private int port = 1119;
    Channel channel;
    public NettyClient(String host,int port){
        this.ip=host;
        this.port = port;
    }
    public void connect() {
        //创建客户端channel的辅助类,发起connection请求
        ClientBootstrap bootstrap = new ClientBootstrap(
                new NioClientSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));
        //It means one same HelloWorldClientHandler instance is going to handle multiple Channels and consequently the data will be corrupted.
        //基于上面这个描述，必须用到ChannelPipelineFactory每次创建一个pipeline
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() {
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("decoder", new ProviderDecoder());
                pipeline.addLast("encoder", new ProviderEncoder());
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
        InetSocketAddress address = new InetSocketAddress(ip, port);
        // InetSocketAddress i = new InetSocketAddress( port);
        ChannelFuture future = bootstrap.connect(address);
        channel = future.getChannel();

    }

    public void write(final FalconRequest request){
        ChannelFuture future  = channel.write(request);
        future.addListener(new ChannelFutureListener(){

            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if(!future.isSuccess()){
                    future.getCause().printStackTrace();
                    InvokerContext context = CustomerManager.requestIng.get(request.getSequence());
                    context.getCallBack().processFailedResponse(future.getCause());
                    CustomerManager.requestIng.remove(request.getSequence());
                }
            }
        });
    }


    public void close() {
        channel.close();
        bootstrap.releaseExternalResources();
    }
}