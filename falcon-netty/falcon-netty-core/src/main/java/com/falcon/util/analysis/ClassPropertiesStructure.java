package com.falcon.util.analysis;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by fanshuai on 18/10/28.
 */
public class ClassPropertiesStructure {
    /**
     * 当前类的类型
     */
    private transient Type type;
    /**
     * 类的完整路径
     */
    private String typeName;
    /**
     * 此类所有的属性集合
     */
    private List<PropertyStructure> propertyStructures;


    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public List<PropertyStructure> getPropertyStructures() {
        return propertyStructures;
    }

    public void setPropertyStructures(List<PropertyStructure> propertyStructures) {
        this.propertyStructures = propertyStructures;
    }
}
