package com.falcon.config;

import lombok.Data;
import lombok.Setter;

/**
 * Created by fanshuai on 15-2-7.
 */
@Data
public class ProviderConfig<T> {
    /**
     * 服务api接口
     */
    private Class<T> serviceInterface;
    /**
     * 服务的实现
     */
    private T service;
    /**
     * 当前服务器配置信息
     */
    private ServerConfig serverConfig;
    /**
     * domain
     */
    @Setter
    private String domain;
    /**
     * 分组
     */
    @Setter
    private String group = "default";
}
