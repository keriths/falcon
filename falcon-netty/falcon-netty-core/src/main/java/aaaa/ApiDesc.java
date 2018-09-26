package aaaa;

import java.beans.Transient;
import java.io.Serializable;
import java.util.List;

/**
 * Created by fanshuai on 18/9/25.
 */
public class ApiDesc implements Serializable{
    /**
     * 接口的唯一ID,如未指定使用serviceName+"#"+methodName+"#"+paramNames
     */
    private String apiId;
    /**
     * 服务类简单名称
     */
    private String serviceTypeSingleName;
    /**
     * 服务类全路径
     */
    private String serviceTypeFullName;
    /**
     * 服务描述
     */
    private String serviceDesc;
    /**
     * 接口方法描述列表
     */
    List<ApiMethodDesc> apiMethodDescList;
    /**
     * 服务实现的实例，服务实时调用时使用的
     */
    private transient Object serviceObj;


    public String getApiId() {
        if (apiId==null||"".equals(apiId)){
            apiId = serviceTypeFullName;
        }
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getServiceTypeSingleName() {
        return serviceTypeSingleName;
    }

    public void setServiceTypeSingleName(String serviceTypeSingleName) {
        this.serviceTypeSingleName = serviceTypeSingleName;
    }

    public String getServiceTypeFullName() {
        return serviceTypeFullName;
    }

    public void setServiceTypeFullName(String serviceTypeFullName) {
        this.serviceTypeFullName = serviceTypeFullName;
    }

    public String getServiceDesc() {
        return serviceDesc;
    }

    public void setServiceDesc(String serviceDesc) {
        this.serviceDesc = serviceDesc;
    }

    public List<ApiMethodDesc> getApiMethodDescList() {
        return apiMethodDescList;
    }

    public void setApiMethodDescList(List<ApiMethodDesc> apiMethodDescList) {
        this.apiMethodDescList = apiMethodDescList;
    }

    public Object getServiceObj() {
        return serviceObj;
    }

    public void setServiceObj(Object serviceObj) {
        this.serviceObj = serviceObj;
    }
}
