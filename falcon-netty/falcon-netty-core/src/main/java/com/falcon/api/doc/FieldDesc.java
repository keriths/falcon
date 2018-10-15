package com.falcon.api.doc;

import java.io.Serializable;

/**
 * Created by fanshuai on 17/11/8.
 */
public class FieldDesc implements Serializable {
    private String fieldTypeFullName;
    private String fieldTypeSingleName;
    private String fieldName;
    private String fieldDesc;


    public String getFieldTypeFullName() {
        return fieldTypeFullName;
    }

    public void setFieldTypeFullName(String fieldTypeFullName) {
        this.fieldTypeFullName = fieldTypeFullName;
    }

    public String getFieldTypeSingleName() {
        return fieldTypeSingleName;
    }

    public void setFieldTypeSingleName(String fieldTypeSingleName) {
        this.fieldTypeSingleName = fieldTypeSingleName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldDesc() {
        return fieldDesc;
    }

    public void setFieldDesc(String fieldDesc) {
        this.fieldDesc = fieldDesc;
    }
}
