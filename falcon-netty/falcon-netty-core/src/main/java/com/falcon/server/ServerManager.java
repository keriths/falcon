package com.falcon.server;

import com.falcon.config.ServerConfig;
import com.falcon.server.netty.NettyServer;
import com.falcon.server.netty.Server;

import java.net.UnknownHostException;

/**
 * Created by fanshuai on 15-2-7.
 */
public class ServerManager {
    private static NettyServer nettyServer = new NettyServer();
    public static ServerConfig startServer(ServerConfig serverConfig) throws UnknownHostException {
        nettyServer.setActuryPort(serverConfig.getPort());
        nettyServer.start();
        serverConfig.setPort(nettyServer.getActuryPort());
        serverConfig.setIp(nettyServer.getIp());
        return serverConfig;
    }
}
