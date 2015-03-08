package com.falcon.client;

import com.falcon.server.servlet.FalconRequest;

/**
 * Created by fanshuai on 15-2-11.
 */
public class InvokerContext {

    public FalconRequest request;
    public CustomerConfig customerConfig;
    public InvokerCallBack callBack;
    public InvokerContext(FalconRequest request, CustomerConfig customerConfig, InvokerCallBack callBack) {
        this.request=request;
        this.customerConfig = customerConfig;
        this.callBack=callBack;
    }

    public FalconRequest getRequest() {
        return request;
    }

    public void setRequest(FalconRequest request) {
        this.request = request;
    }

    public CustomerConfig getCustomerConfig() {
        return customerConfig;
    }

    public void setCustomerConfig(CustomerConfig customerConfig) {
        this.customerConfig = customerConfig;
    }

    public InvokerCallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(InvokerCallBack callBack) {
        this.callBack = callBack;
    }
}
