package com.falcon.server.jetty;

import com.falcon.server.netty.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import java.net.UnknownHostException;

/**
 * Created by fanshuai on 18/9/27.
 */
public class JettyServer implements Server {
    private org.eclipse.jetty.server.Server server;
    private ServletContextHandler context;
    private Object serverStartingLock = new Object();
    public JettyServer(int port,String servletContext){
        server = new org.eclipse.jetty.server.Server(port);
        context = new ServletContextHandler(server, servletContext);
        server.setHandler(context);
    }
    @Override
    public boolean isStarted() {
        if (server==null){
            return false;
        }
        return server.isStarted();
    }

    @Override
    public void start() throws UnknownHostException {
        if (isStarted()) {
            return;
        }
        synchronized (serverStartingLock) {
            if (isStarted()) {
                return;
            }
            try {
                server.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            while (!server.isStarted()) {
                try {
                    Thread.sleep(100l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        context.addServlet(ApiInvokerJettyServlet.class, "/api/invoke");
        context.addServlet(ApiDocJettyServlet.class, "/api/doc");
    }

    @Override
    public Object doRequest(Object param) {
        return null;
    }
}
