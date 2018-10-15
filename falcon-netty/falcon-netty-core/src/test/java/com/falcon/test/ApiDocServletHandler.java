package com.falcon.test;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by fanshuai on 18/9/25.
 */
public class ApiDocServletHandler extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject ret =  new JSONObject();
        try {
            String ttsTxt = req.getParameter("text");

            String outFile = System.nanoTime() + ".mp4";
            ret.put("ret","0");
        }catch (Exception ex){
            ret.put("ret","-1");
            ret.put("error",ex.getMessage());
        }
        if(req.getParameter("callback")!=null) {
            resp.getWriter().write(req.getParameter("callback")+"("+ret.toString()+")");
        }else {
            resp.getWriter().write(ret.toString());
        }
    }
}
