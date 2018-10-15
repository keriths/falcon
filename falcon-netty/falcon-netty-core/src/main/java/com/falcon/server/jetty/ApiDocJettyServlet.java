package com.falcon.server.jetty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.falcon.api.doc.ApiDescManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by fanshuai on 18/9/27.
 */
public class ApiDocJettyServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        JSONObject ret =  new JSONObject();
//        try {
//            String ttsTxt = req.getParameter("text");
//            String outFile = System.nanoTime() + ".mp4";
//            ret.put("ret","0");
//        }catch (Exception ex){
//            ret.put("ret","-1");
//            ret.put("error",ex.getMessage());
//        }
        resp.getWriter().write(JSON.toJSONString(ApiDescManager.getAllApiDesc()));
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write(JSON.toJSONString(ApiDescManager.getAllApiDesc()));
    }
}
