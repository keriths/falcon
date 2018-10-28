package com.falcon.util.analysis;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * Created by fanshuai on 18/10/28.
 */
public class ParamStructure implements Serializable{
    /**
     * 参数名称
     */
    private String paramName;
    /**
     * 参数描述，用于生成文档，对属性的说明
     */
    private String paramDesc;
    /**
     * 参数类型
     */
    private String paramTypeName;
    private transient Type paramType;

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamDesc() {
        return paramDesc;
    }

    public void setParamDesc(String paramDesc) {
        this.paramDesc = paramDesc;
    }

    public String getParamTypeName() {
        return paramTypeName;
    }

    public void setParamTypeName(String paramTypeName) {
        this.paramTypeName = paramTypeName;
    }

    public Type getParamType() {
        return paramType;
    }

    public void setParamType(Type paramType) {
        this.paramType = paramType;
    }
}
