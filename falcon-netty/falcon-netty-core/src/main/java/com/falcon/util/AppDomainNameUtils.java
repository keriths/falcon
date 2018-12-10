package com.falcon.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by fanshuai on 18/12/10.
 */
public class AppDomainNameUtils {
    private static String appDomainPropertiesFileName="appdomain.properties";
    private static String domainName;
    static {
        InputStream inputStream = AppDomainNameUtils.class.getClassLoader().getResourceAsStream(appDomainPropertiesFileName);
        Properties prop = new Properties();
        try {
            prop.load(inputStream);
            domainName = prop.getProperty("domainName");
            if (domainName==null||domainName.trim().length()==0){
                throw new RuntimeException("domainName not found please set appdomain.properties");
            }
        } catch (IOException e) {
            throw new RuntimeException("get domainName from appdomain.properties exception ",e);
        }
    }

    public static String getDomainName() {
        return domainName;
    }

}
