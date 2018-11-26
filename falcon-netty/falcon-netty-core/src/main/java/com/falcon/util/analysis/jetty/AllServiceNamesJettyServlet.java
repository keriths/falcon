package com.falcon.util.analysis.jetty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.falcon.api.doc.ApiDescManager;
import com.falcon.util.analysis.ServiceManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by fanshuai on 18/9/27.
 */
public class AllServiceNamesJettyServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write(JSON.toJSONString(ServiceManager.getServiceNames()));
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }
}
