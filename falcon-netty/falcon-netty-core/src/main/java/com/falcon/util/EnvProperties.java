package com.falcon.util;

import com.sun.tools.javac.comp.Env;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by fanshuai on 15/5/9.
 */
public class EnvProperties {
    private static String envFilePath="/data/env/env.properties";
    private static Properties prop = new Properties();
    static {
        InputStream in  = null;
        try {
            File file = new File(envFilePath);
            if (file.exists()&&!file.isDirectory()){
                in = new FileInputStream(file);
                if(in!=null) {
                    prop.load(in);
                }
            }else {
                throw new RuntimeException("not found env properties file :"+file.getAbsolutePath());
            }
        } catch (Exception e) {
            throw new RuntimeException("init env properties error :"+e.getMessage());
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
    public static String get(String key){
        return (String)prop.get(key);
    }

    public static void main(String[] args){
        System.out.println(EnvProperties.get("evn"));
    }
}
