package com.req;

import java.io.Serializable;

/**
 * Created by fanshuai on 15-2-3.
 */
public class Request implements Serializable{
    private String msg;
    public Request(){}
    public Request(String msg){
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Request:msg is "+msg;
    }
}
