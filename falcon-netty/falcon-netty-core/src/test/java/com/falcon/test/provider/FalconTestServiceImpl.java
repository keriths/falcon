package com.falcon.test.provider;

import com.falcon.test.api.FalconTestService;

/**
 * Created by fanshuai on 15-2-7.
 */
public class FalconTestServiceImpl implements FalconTestService {
    @Override
    public String hello(String name) {
        return "hello:"+name;
    }
}
