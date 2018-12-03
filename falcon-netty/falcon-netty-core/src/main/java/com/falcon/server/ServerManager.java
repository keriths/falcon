package com.falcon.server;

import com.falcon.config.ServerConfig;
import com.falcon.server.netty.NettyServer;
import com.falcon.server.netty.Server;
import com.falcon.util.analysis.jetty.JettyServer;
import com.google.common.collect.Lists;

import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by fanshuai on 15-2-7.
 */
public class ServerManager {
    public static String TCP = "TCP";
    public static String HTTP = "HTTP";

    private static NettyServer nettyServer = new NettyServer();
    private static JettyServer jettyServer;
    public static ServerConfig startServer(ServerConfig serverConfig) throws UnknownHostException {
        nettyServer.setActuryPort(serverConfig.getPort());
        nettyServer.start();
        serverConfig.setPort(nettyServer.getActuryPort());
        serverConfig.setIp(nettyServer.getIp());

        jettyServer = new JettyServer(8888,"/");
        jettyServer.start();
        return serverConfig;
    }
    public static List<Server> enableServers(){
        return Lists.newArrayList(nettyServer,jettyServer);
    }
}
