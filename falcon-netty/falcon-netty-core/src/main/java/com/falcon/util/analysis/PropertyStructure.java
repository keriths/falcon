package com.falcon.util.analysis;

import java.io.Serializable;

/**
 * Created by fanshuai on 18/10/28.
 */
public class PropertyStructure implements Serializable{
    /**
     * 属性名称
     */
    private String propertyName;
    /**
     * 属性描述，用于生成文档，对属性的说明
     */
    private String propertyDesc;
    /**
     * 属性对应的类的结构
     */
    private String propertyType;

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyDesc() {
        return propertyDesc;
    }

    public void setPropertyDesc(String propertyDesc) {
        this.propertyDesc = propertyDesc;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }
}
