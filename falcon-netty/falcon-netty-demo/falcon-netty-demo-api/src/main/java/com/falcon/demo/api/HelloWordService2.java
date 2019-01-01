package com.falcon.demo.api;

import com.falcon.demo.api.dto.HelloDTO;
import com.falcon.util.analysis.annotation.DESC;

import java.util.List;

/**
 * Created by fanshuai on 15/4/7.
 */

public interface HelloWordService2 {
    @DESC(value = "sayHello")
    String sayHello(String name,String bb);
    @DESC(value = "hello")
    public HelloDTO hello(HelloDTO helloDTO);
}
