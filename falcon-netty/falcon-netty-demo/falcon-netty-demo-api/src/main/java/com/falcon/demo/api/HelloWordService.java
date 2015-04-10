package com.falcon.demo.api;

import com.falcon.demo.api.dto.HelloDTO;

/**
 * Created by fanshuai on 15/4/7.
 */
public interface HelloWordService {
    String sayHello(String name);
    HelloDTO hello(HelloDTO helloDTO);
}
