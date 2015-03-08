package com.fs.falcon.server.hessian;

import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

/**
 * Created by fanshuai on 15-1-15.
 */
public class FalconHessianProviderServlet extends DispatcherServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        HessianServerConfig.setContextPath(config.getServletContext().getContextPath());
        HessianServerConfig.setDomain(config.getServletContext().getServletContextName());
        super.init(config);
    }
}
