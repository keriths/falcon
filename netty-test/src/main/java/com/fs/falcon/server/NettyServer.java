package com.fs.falcon.server;

import com.fs.falcon.seria.Provider1Decoder;
import com.fs.falcon.seria.Provider1Encoder;
import com.fs.falcon.seria.ProviderDecoder;
import com.fs.falcon.seria.ProviderEncoder;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.jboss.netty.buffer.ChannelBuffers.dynamicBuffer;

/**
 * Created by fanshuai on 15-1-23.
 */
public class NettyServer implements Server {
    private static Object startLock = new Object();
    private int acturyPort;
    private Channel channel;
    private boolean started = false;
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
            String ip = InetAddress.getLocalHost().getHostAddress();
            int port = getAvailablePort(1119,InetAddress.getLocalHost());
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
            serverBootstrap.setOption("child.reuseAddress", true);
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
    public static int getAvailablePort(int defaultPort,InetAddress host) {
        int port = defaultPort;
        while (port < 65535) {
            if (!isPortInUse(port,host)) {
                return port;
            } else {
                port++;
            }
        }
        while (port > 0) {
            if (!isPortInUse(port,host)) {
                return port;
            } else {
                port--;
            }
        }
        throw new IllegalStateException("no available port");
    }

    public static boolean isPortInUse(int port,InetAddress host) {
        boolean inUse = false;
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(port,50,host);
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
}
