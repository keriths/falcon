package com.falcon.client;

import com.falcon.server.servlet.FalconRequest;
import com.falcon.server.servlet.FalconResponse;

/**
 * Created by fanshuai on 15-3-13.
 */
public interface Client {
    /**
     * 发送请求
     * @param request
     * @return
     */
    FalconResponse sendRequest(FalconRequest request);

    /**
     * 客户端是否可用
     * @return
     */
    boolean isAlive();

}
