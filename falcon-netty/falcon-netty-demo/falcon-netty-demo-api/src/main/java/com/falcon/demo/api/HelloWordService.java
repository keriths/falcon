package com.falcon.demo.api;

import com.falcon.demo.api.dto.HelloDTO;

import java.util.List;

/**
 * Created by fanshuai on 15/4/7.
 */
public interface HelloWordService {
    String sayHello(String name);
    public HelloDTO hello(HelloDTO helloDTO);
    public List<HelloDTO> hellolist(List<HelloDTO> helloDTOs);
}
