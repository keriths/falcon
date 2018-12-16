package com.falcon.server.netty;

import com.falcon.client.CustomerManager;
import com.falcon.client.InvokeClient;
import com.falcon.client.InvokerContext;
import com.falcon.server.servlet.FalconRequest;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by fanshuai on 15-2-10.
 */
public class NettyClient  implements InvokeClient{
    private final static Logger log = LoggerFactory.getLogger(NettyClient.class);
    private ClientBootstrap bootstrap;
    private String ip ;
    private int port ;
    Channel channel;
    public NettyClient(String host,int port){
        this.ip=host;
        this.port = port;
        //创建客户端channel的辅助类,发起connection请求
        bootstrap = new ClientBootstrap(
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
        connect();
    }
    public synchronized void connect() {

        //创建无连接传输channel的辅助类(UDP),包括client和server
        InetSocketAddress address = new InetSocketAddress(ip, port);
        // InetSocketAddress i = new InetSocketAddress( port);
        ChannelFuture future = bootstrap.connect(address);
        if(!future.awaitUninterruptibly(500l, TimeUnit.MILLISECONDS)){
            return;
        }
        if(!future.isSuccess()){
            return ;
        }
        Channel newChannel = future.getChannel();
        try {
            Channel oldChannel = this.channel;
            if(oldChannel!=null){
                oldChannel.close();
            }
        } catch (Exception e){

        }finally {
            this.channel = newChannel;
        }
    }

    public boolean isConnected(){
        return channel.isConnected();
    }

    private void write(final FalconRequest request){
        log.info(request+" channel write ");
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
        try {
            channel.close();
            bootstrap.releaseExternalResources();
        }catch (Exception e){
            channel = null;
            bootstrap = null;
        }

    }

    @Override
    public Object doRequest(FalconRequest request, InvokerContext invokerContext) {
        write(request);
        return null;
    }
}