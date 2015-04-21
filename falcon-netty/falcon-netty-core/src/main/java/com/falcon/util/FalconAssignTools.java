package com.falcon.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by fanshuai on 15/4/21.
 */
public class FalconAssignTools {

    private static Properties prop = new Properties();
    static {
        InputStream in  = null;
        try {
            in = FalconAssignTools.class.getClassLoader().getResourceAsStream("falcon.assign.properties");
            if(in!=null) {
                prop.load(in);
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }finally {
            try {
                if(in!=null){
                    in.close();
                }
            } catch (IOException e) {
                in = null;
            }
        }
    }

    public static String getProperty(String key){
        return prop.getProperty(key);
    }
}
