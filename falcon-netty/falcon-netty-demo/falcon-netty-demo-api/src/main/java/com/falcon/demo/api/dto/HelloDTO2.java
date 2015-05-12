package com.falcon.demo.api.dto;

import java.io.Serializable;

/**
 * Created by fanshuai on 15/4/7.
 */
public class HelloDTO2 implements Serializable{
    private String name;
    private String val;

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
