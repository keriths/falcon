package com.falcon.exception;

import com.falcon.spring.ServiceContainer;

/**
 * Created by fanshuai on 15-2-7.
 */
public class ServiceContainerInitException extends Exception {
    public ServiceContainerInitException(){

    }
    public ServiceContainerInitException(String message){
        super(message);
    }
    public ServiceContainerInitException(Throwable cause){
        super(cause);
    }
}
