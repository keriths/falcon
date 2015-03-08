package com.falcon.config;

import lombok.Data;

/**
 * Created by fanshuai on 15-2-7.
 */
@Data
public class ServerConfig {
    /**
     * 服务提供者IP
     */
    private String ip;
    /**
     * 服务提供者端口
     */
    private int port;
    /**
     * 协议
     */
    private String protocol;
}
