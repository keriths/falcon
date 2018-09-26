package aaaa;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fanshuai on 17/10/22.
 */
public class ApiMethodDesc {
    /**
     * 接口方法的唯一ID,如未指定使用serviceName+"#"+methodName+"#"+paramNames
     */
    private String methodId;
    /**
     * 服务类路径
     */
    private String serviceName;
    /**
     * 服务描述
     */
    private String serviceDesc;
    /**
     * 方法名称
     */
    private String methodName;
    /**
     * 接口方法描述
     */
    private String methodDesc;
    /**
     * 参数列表字符形式
     */
    private String paramNames;
    /**
     * 返回类型字符串
     */
    private String returnTypeName;
    /**
     * 参数，参数名称和参数类型映射关系map
     */
    private Map<String,String> paramNameParamTypeNameMap;
    /**
     * 此方法中用到的类名称和类结构映射关系map
     */
    private Map<String,TypeDesc> typeFieldDetailLinkedHashMap = new HashMap<String, TypeDesc>();





    /**
     * 服务实现的实例，服务实时调用时使用的
     */
    private transient Object serviceObj;
    /**
     * 接口方法实例，服务实时调用时使用的
     */
    private transient Method method;
    /**
     * 方法参数类型列表，服务实时调用时使用的
     */
    private transient Type[] paramTypes;
    /**
     * 返回类型
     */
    private transient Type returnType;



    public Map<String, TypeDesc> getTypeFieldDetailLinkedHashMap() {
        return typeFieldDetailLinkedHashMap;
    }

    public void setTypeFieldDetailLinkedHashMap(Map<String, TypeDesc> typeFieldDetailLinkedHashMap) {
        this.typeFieldDetailLinkedHashMap = typeFieldDetailLinkedHashMap;
    }


    public String getServiceDesc() {
        return serviceDesc;
    }

    public void setServiceDesc(String serviceDesc) {
        this.serviceDesc = serviceDesc;
    }

    public String getMethodDesc() {
        return methodDesc;
    }

    public void setMethodDesc(String methodDesc) {
        this.methodDesc = methodDesc;
    }

    public Object getServiceObj() {
        return serviceObj;
    }

    public void setServiceObj(Object serviceObj) {
        this.serviceObj = serviceObj;
    }

    public String getMethodId() {
        if (methodId==null){
            methodId=serviceName+"#"+methodName+"#"+paramNames.replaceAll(" ","");
        }
        return methodId;
//        return methodId;
    }

    public void setMethodId(String methodId) {
        this.methodId = methodId;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getParamNames() {
        return paramNames;
    }

    public void setParamNames(String paramNames) {
        this.paramNames = paramNames;
    }

    public String getReturnTypeName() {
        return returnTypeName;
    }

    public void setReturnTypeName(String returnTypeName) {
        this.returnTypeName = returnTypeName;
    }

    public Map<String, String> getParamNameParamTypeNameMap() {
        return paramNameParamTypeNameMap;
    }

    public void setParamNameParamTypeNameMap(Map<String, String> paramNameParamTypeNameMap) {
        this.paramNameParamTypeNameMap = paramNameParamTypeNameMap;
    }

    public Type[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(Type[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

}