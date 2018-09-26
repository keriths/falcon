package aaaa;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fanshuai on 17/11/8.
 */
public class TypeDesc implements Serializable {
    private String typeFullName;
    private String typeSingleName;
    private List<FieldDesc> fieldDescList;

    public String getTypeFullName() {
        return typeFullName;
    }

    public void setTypeFullName(String typeFullName) {
        this.typeFullName = typeFullName;
    }

    public String getTypeSingleName() {
        return typeSingleName;
    }

    public void setTypeSingleName(String typeSingleName) {
        this.typeSingleName = typeSingleName;
    }

    public List<FieldDesc> getFieldDescList() {
        return fieldDescList;
    }

    public void setFieldDescList(List<FieldDesc> fieldDescList) {
        this.fieldDescList = fieldDescList;
    }
}
