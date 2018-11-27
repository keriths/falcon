package com.falcon.demo.api.dto;

import com.falcon.util.analysis.annotation.DESC;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by fanshuai on 15/4/7.
 */
public class HelloDTO implements Serializable {
    @DESC(value = "名称")
    private String name;
    @DESC(value = "value")
    private String val;
    @DESC(value = "bigdecimal")
    public BigDecimal b ;
    @DESC(value = "HelloDTO2")
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
