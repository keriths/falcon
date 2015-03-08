package com.falcon.client;

/**
 * Created by fanshuai on 15-2-11.
 */
public class InvokerZKClientFactory {
    private static InvokerZKClient client ;
    public static InvokerZKClient getClient() throws Exception {
        if (client==null){
            synchronized (InvokerZKClientFactory.class){
                if(client==null){
                    client = new InvokerZKClient();
                }
            }
        }
        return client;
    }
}
