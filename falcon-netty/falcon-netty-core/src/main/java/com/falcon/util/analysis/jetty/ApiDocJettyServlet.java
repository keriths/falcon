package com.falcon.util.analysis.jetty;

import com.alibaba.fastjson.JSON;
import com.falcon.util.analysis.ServiceManager;
import com.falcon.util.analysis.ServiceStructureInfo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//import freemarker.template.Template;

/**
 * Created by fanshuai on 18/9/27.
 */
public class ApiDocJettyServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");
        String serviceKey = req.getParameter("services");
        String method = req.getParameter("method");
        if (serviceKey!=null && serviceKey.length()>0 && method!=null && method.length()>0){
            ServiceStructureInfo serviceStructureInfo = ServiceManager.getServiceStructureInfo(serviceKey);
            if (serviceStructureInfo==null){
                return;
            }
            resp.getWriter().write(JSON.toJSONString(serviceStructureInfo.getServiceMethodStructureInfo(method)));
            return;
        }
        if (serviceKey!=null && serviceKey.length()>0){
            resp.getWriter().write(JSON.toJSONString(ServiceManager.getServiceStructureInfo(serviceKey)));
            return;
        }
        resp.getWriter().write(JSON.toJSONString(ServiceManager.getServiceStructureInfoMap().values()));
        return;
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }
}
