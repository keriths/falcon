package com.falcon.server;

import com.falcon.server.servlet.FalconRequest;
import com.falcon.server.servlet.FalconResponse;

/**
 * Created by fanshuai on 15-3-13.
 */
public interface Server {
    /**
     * 服务是否启动状态
     * @return
     */
    boolean isStarted();

    /**
     * 启动服务
     */
    void start();

    /**
     * 停止服务
     */
    void stop();

    /**
     * 接收处理的请求
     * @param request
     * @return
     */
    FalconResponse doRequest(FalconRequest request);

}
