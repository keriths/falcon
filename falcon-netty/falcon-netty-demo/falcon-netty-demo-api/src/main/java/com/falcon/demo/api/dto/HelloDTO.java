package com.falcon.demo.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by fanshuai on 15/4/7.
 */
public class HelloDTO implements Serializable {
    private String name;
    private String val;
    public BigDecimal b ;
    private HelloDTO2 h2 = new HelloDTO2();

    public HelloDTO2 getH2() {
        return h2;
    }

    public void setH2(HelloDTO2 h2) {
        this.h2 = h2;
    }

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

    public BigDecimal getB() {
        return b;
    }

    public void setB(BigDecimal b) {
        this.b = b;
    }

    @Override
    public String toString() {
        return b.toString();
    }
}
