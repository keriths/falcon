package com.falcon.util.analysis.jetty;

import com.alibaba.fastjson.JSON;
import com.falcon.util.analysis.ServiceManager;
import com.falcon.util.analysis.ServiceStructureInfo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
//import freemarker.template.Template;

/**
 * Created by fanshuai on 18/9/27.
 */
public class HtmlJettyServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=utf-8");
        String fileName = req.getParameter("fileName");
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName+".html");
        PrintWriter writer = resp.getWriter();
        while (true){
            byte[] buffer = new byte[1024];
            int size = inputStream.read(buffer);
            if (size==-1){
                break;
            }
            writer.write(new String(buffer,0,size));
        }
        inputStream.close();
        writer.flush();
        return;
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }
}
