package com.falcon.demo.provider;

import com.falcon.demo.api.HelloWordService;
import com.falcon.demo.api.dto.HelloDTO;

/**
 * Created by fanshuai on 15/4/7.
 */
public class HelloWordServiceImpl implements HelloWordService {
    @Override
    public String sayHello(String name) {
        return "hello : "+name;
    }

    @Override
    public HelloDTO hello(HelloDTO helloDTO) {
        if(helloDTO==null){
            return new HelloDTO();
        }
        HelloDTO helloDTO1 = new HelloDTO();
        helloDTO1.setName("hello : "+helloDTO.getName());
        return helloDTO1;
    }
}
