package com.falcon.server.netty;

import lombok.Data;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;

/**
 * Created by fanshuai on 15-1-23.
 */
public class NettyServer implements Server {
    private static Object startLock = new Object();
    public int acturyPort;
    private Channel channel;
    private boolean started = false;
    private String ip;
    private ServerBootstrap serverBootstrap ;
    @Override
    public boolean isStarted() {
        return started;
    }

    @Override
    public void start() throws UnknownHostException {
        if(started){
            return;
        }
        synchronized (startLock){
            if(started){
                return ;
            }
            int port = getAvailablePort(acturyPort);
            ip = InetAddress.getLocalHost().getHostAddress();
            InetSocketAddress address = new InetSocketAddress(ip,port);
            acturyPort = port;
            ServerBootstrap serverBootstrap = new ServerBootstrap(
                    new NioServerSocketChannelFactory(
                            Executors.newCachedThreadPool(),
                            Executors.newCachedThreadPool()));
            serverBootstrap.setPipelineFactory(new ChannelPipelineFactory() {
                public ChannelPipeline getPipeline() {
                    ChannelPipeline pipeline = Channels.pipeline();
                    pipeline.addLast("decoder", new ProviderDecoder());
                    pipeline.addLast("encoder", new ProviderEncoder());
                    pipeline.addLast("handler", new NettyServerChannelHandler());
                    return pipeline;
                }
            });
            serverBootstrap.setOption("child.tcpNoDelay", true);
            serverBootstrap.setOption("child.keepAlive", true);

            //创建服务器端channel的辅助类,接收connection请求
            serverBootstrap.bind(address);
            this.started=true;
            System.out.println("netty server started :"+address.getAddress().getHostAddress()+":"+address.getPort());
        }
    }
    public void close(){
        if(started){
            this.serverBootstrap.releaseExternalResources();
            this.channel.unbind();
        }
    }
    public static int getAvailablePort(int defaultPort) {
        int port = defaultPort;
        while (port < 65535) {
            if (!isPortInUse(port)) {
                return port;
            } else {
                port++;
            }
        }
        while (port > 0) {
            if (!isPortInUse(port)) {
                return port;
            } else {
                port--;
            }
        }
        throw new IllegalStateException("no available port");
    }

    public static boolean isPortInUse(int port) {
        boolean inUse = false;
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(port);
            inUse = false;
        } catch (IOException e) {
            inUse = true;
        } finally {
            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                }
            }
        }
        return inUse;
    }


    @Override
    public Object doRequest(Object param) {
        return null;
    }

    public int getActuryPort() {
        return acturyPort;
    }

    public void setActuryPort(int acturyPort) {
        this.acturyPort = acturyPort;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
