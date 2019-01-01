package com.falcon.demo.api;

import com.falcon.demo.api.dto.HelloDTO;
import com.falcon.util.analysis.annotation.DESC;

import java.util.List;

/**
 * Created by fanshuai on 15/4/7.
 */

public interface HelloWordService {
    @DESC(value = "sayHello")
    String sayHello(String name,String bb);
    @DESC(value = "hellolist")
    public List<HelloDTO> hellolist(List<HelloDTO> helloDTOs);
}
