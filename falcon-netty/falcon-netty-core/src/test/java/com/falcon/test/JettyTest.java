package com.falcon.test;

import com.falcon.api.doc.ApiDesc;
import com.falcon.api.doc.ApiDescManager;
import com.falcon.api.doc.TestObject;
import com.falcon.server.jetty.JettyServer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import java.net.UnknownHostException;

/**
 * Created by fanshuai on 18/9/25.
 */
public class JettyTest {
    public static void main(String[] args){
        ApiDesc apiDesc = ApiDescManager.parseServiceObject(new TestObject(), "");
        ApiDescManager.addApiDesc(apiDesc);
        JettyServer jettyServer = new JettyServer(1115,"/");
        try {
            jettyServer.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
//
//        Server server = new Server(11111);
//        ServletContextHandler context=new ServletContextHandler(server,"/");
//        server.setHandler(context);
//        context.addServlet(JettyServletHandler.class,"/test");
//
//        try {
//            server.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        ;
//        try {
//            server.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        try {
            Thread.sleep(1000*60*60*60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
