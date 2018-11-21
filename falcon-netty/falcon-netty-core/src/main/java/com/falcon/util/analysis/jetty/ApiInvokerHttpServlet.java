package com.falcon.util.analysis.jetty;

import com.alibaba.fastjson.JSON;
import com.falcon.util.analysis.RequestDTO;
import com.falcon.util.analysis.test.ServiceParseInvokeTest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanshuai on 18/11/21.
 */
public class ApiInvokerHttpServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestBody = getRequestContentString(req);
        RequestDTO requestDTO = JSON.toJavaObject(JSON.parseObject(requestBody), RequestDTO.class);
        Object obj = ServiceParseInvokeTest.process(requestDTO);
        resp.getWriter().write(JSON.toJSONString(obj));
        resp.flushBuffer();
    }

    private String getRequestContentString(HttpServletRequest req) throws IOException{
        byte[] requestBodyBytes = getRequestContentBytes(req);
        if (requestBodyBytes==null){
            return null;
        }
        return new String(requestBodyBytes);
    }

    private byte[] getRequestContentBytes(HttpServletRequest req) throws IOException {
        if (req.getContentLength()==-1){
            return null;
        }
        InputStream in =  req.getInputStream();
        byte[] requestBodyBytes = new byte[req.getContentLength()];
        byte[] buffer = new byte[1024];
        int post = 0;
        int length = 0;
        while ((length = in.read(buffer))!=-1){
            for (int i = 0;i<length;i++){
                requestBodyBytes[post]=buffer[i];
                post++;
            }
        }
        return requestBodyBytes;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }
}
