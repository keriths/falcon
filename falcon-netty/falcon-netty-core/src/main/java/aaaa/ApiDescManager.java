package aaaa;

import com.alibaba.fastjson.JSON;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.util.CollectionUtils;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by fanshuai on 18/9/25.
 */
public class ApiDescManager {
    private static Map<String,ApiMethodDesc> allApiMethodDesc = new HashMap<String, ApiMethodDesc>();
    private static Map<String,ApiDesc> allApiDesc = new HashMap<String, ApiDesc>();

    public static void addApiDesc(ApiDesc apiDesc){
        allApiDesc.put(apiDesc.getApiId(),apiDesc);
        for (ApiMethodDesc apiMethodDesc:apiDesc.getApiMethodDescList()){
            allApiMethodDesc.put(apiMethodDesc.getMethodId(),apiMethodDesc);
        }
    }

    public static ApiMethodDesc getApiMethodDesc(String methodId){
        return allApiMethodDesc.get(methodId);
    }

    public static void main(String[] args){
        ApiDesc apiDesc = new ApiDescManager().parseServiceObject(new TestObject(),"");
        addApiDesc(apiDesc);
        System.out.println(JSON.toJSONString(apiDesc));
    }
    public static List<String> objectPublicMethodNameList(){
        Method[] methods = Object.class.getMethods();
        List<String> methodNames = new ArrayList<String>();
        for (Method method : methods){
            methodNames.add(method.getName());
        }
        return methodNames;
    }
    public ApiDesc parseServiceObject(Object bean,String serviceId){
        Object serviceObj = bean;
        ApiDesc apiDesc = getApiDesc(bean, serviceId);
        Class serviceClass = serviceObj.getClass();
        Method[] methods = serviceClass.getMethods();
        List<ApiMethodDesc> apiMethodDescList = new ArrayList<ApiMethodDesc>(methods.length);
        for (Method method : methods){
            if (objectPublicMethodNameList().contains(method.getName())){
                continue;
            }
            ApiMethodDesc apiMethodDesc = getApiMethodDesc(apiDesc,serviceObj, method);
            apiMethodDescList.add(apiMethodDesc);
        }
        apiDesc.setApiMethodDescList(apiMethodDescList);
        return apiDesc;
    }

    private ApiMethodDesc getApiMethodDesc(ApiDesc apiDesc, Object serviceObj, Method method) {
        ApiMethodDesc apiMethodDesc = new ApiMethodDesc();
        apiMethodDesc.setMethodId("");
        apiMethodDesc.setServiceName(serviceObj.getClass().getName());
        apiMethodDesc.setServiceDesc(apiDesc.getServiceDesc());
        apiMethodDesc.setMethodName(method.getName());
        apiMethodDesc.setMethodDesc(getApiDocDesc(method.getAnnotation(ApiDoc.class)));
        LinkedHashMap<String, String> paramNameParamTypeNameMap = getParamNameParamTypeNameMap(method);
        apiMethodDesc.setParamNames(paramNameParamTypeNameMap.values().toString());
        apiMethodDesc.setParamTypes(method.getGenericParameterTypes());
        apiMethodDesc.setReturnTypeName(method.getGenericReturnType().getTypeName());
        apiMethodDesc.setReturnType(method.getGenericReturnType());
        apiMethodDesc.setParamNameParamTypeNameMap(paramNameParamTypeNameMap);
        apiMethodDesc.setTypeFieldDetailLinkedHashMap(getTypeFieldDetailLinkedHashMap(method));
        return apiMethodDesc;
    }

    private Map<String, TypeDesc> getTypeFieldDetailLinkedHashMap(Method method) {
        Map<String,TypeDesc> typeFieldDetailLinkedHashMap = new HashMap<String, TypeDesc>();
        for (Type type :method.getGenericParameterTypes()){
            getApiMethodResultType(type,typeFieldDetailLinkedHashMap);
        }
        return typeFieldDetailLinkedHashMap;
    }

    private TypeDesc getApiMethodResultType(Type genericReturnType,Map<String,TypeDesc> typeFieldDetailLinkedHashMap) {
        TypeDesc apiMethodResultType = new TypeDesc();
        apiMethodResultType.setTypeFullName(getTypeSimpleName(genericReturnType));
        apiMethodResultType.setTypeSingleName(getTypeFullName(genericReturnType));
        if (isOwnerType(genericReturnType)){
            typeDetail(genericReturnType,typeFieldDetailLinkedHashMap);
        }else {
            //其它返回
        }
        return apiMethodResultType;
    }

    private void typeDetail(Type type,Map<String,TypeDesc> typeFieldDetailLinkedHashMap){
        String typeFullName=getTypeFullName(type);
        String typeSingleName=getTypeSimpleName(type);
        if (typeFieldDetailLinkedHashMap.containsKey(typeFullName)){
            return;
        }
        TypeDesc typeDetail = new TypeDesc();
        typeDetail.setTypeFullName(typeFullName);
        typeDetail.setTypeSingleName(typeSingleName);
        typeFieldDetailLinkedHashMap.put(typeFullName,typeDetail);
        List<Field> fields = getAllFields(type);

        if (CollectionUtils.isEmpty(fields)){
            return;
        }
        List<FieldDesc> fieldDescList = new ArrayList<FieldDesc>();
        typeDetail.setFieldDescList(fieldDescList);
        for (Field field:fields){
            FieldDesc fieldDesc = new FieldDesc();

            Type genericType = field.getGenericType();

            ApiDoc apiResultFieldDesc=field.getAnnotation(ApiDoc.class);
            String desc = "";
            if (apiResultFieldDesc!=null){
                desc=apiResultFieldDesc.desc();
            }
            String filedName = field.getName();
            String fieldTypeName = getTypeName(genericType,typeFieldDetailLinkedHashMap);
            fieldDesc.setFieldDesc(desc);
            fieldDesc.setFieldTypeFullName(getTypeFullName(genericType));
            fieldDesc.setFieldTypeSingleName(fieldTypeName);
            fieldDesc.setFieldName(filedName);
            fieldDescList.add(fieldDesc);
        }
    }

    private String getTypeName(Type type,Map<String,TypeDesc> typeFieldDetailLinkedHashMap){
        if (isOwnerType(type) ){
            typeDetail(type,typeFieldDetailLinkedHashMap);
        }
        StringBuilder stringBuilder = new StringBuilder();
        if (type instanceof ParameterizedType){
            String fieldTypeName = getTypeSimpleName(type);
            stringBuilder.append(fieldTypeName);
            Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
            if (actualTypeArguments==null||actualTypeArguments.length==0){
                return stringBuilder.toString();
            }
            stringBuilder.append("<");
            int index=0;
            for (Type actualType:actualTypeArguments){
                if (index==0){
                    stringBuilder.append(getTypeName(actualType,typeFieldDetailLinkedHashMap));
                }else {
                    stringBuilder.append(",").append(getTypeName(actualType,typeFieldDetailLinkedHashMap));
                }
                index++;
            }
            stringBuilder.append(">");
            return stringBuilder.toString();
        }else {
            String fieldTypeName = getTypeSimpleName(type);
            stringBuilder.append(fieldTypeName);
            return stringBuilder.toString();
        }

    }

    private List<Field> getAllFields(Type type) {
        List<Field> fieldList = new ArrayList<Field>();
        Field[] declaredFields = ((Class) type).getDeclaredFields();
        if (declaredFields!=null){
            for (Field f:declaredFields){
                fieldList.add(f);
            }
        }
        Field[] fields = ((Class) type).getFields();
        if (fields!=null){
            for (Field f:fields){
                if (fieldList.contains(f)){
                    continue;
                }
                fieldList.add(f);
            }
        }
        return fieldList;

    }

    private boolean isOwnerType(Type type){
        if (getTypeFullName(type).startsWith("java.")){
            return false;
        }
        return true;
    }

    private String getTypeFullName(Type type){
        if (type instanceof Class){
            return ((Class) type).getName();
        }else if (type instanceof ParameterizedTypeImpl){
            return ((ParameterizedTypeImpl)type).getRawType().getName();
        }
        throw new RuntimeException(" not found type ");
    }
    private String getTypeSimpleName(Type type){
        if (type instanceof Class){
            return ((Class) type).getSimpleName();
        }else if (type instanceof ParameterizedTypeImpl){
            return ((ParameterizedTypeImpl)type).getRawType().getSimpleName();
        }
        throw new RuntimeException(" not found type "+type);
    }

    private LinkedHashMap<String, String> getParamNameParamTypeNameMap(Method method) {
        LinkedHashMap<String, String> paramNameParamTypeNameMap = new LinkedHashMap<String, String>();
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = u.getParameterNames(method);
        int i = 0;
        for (Type type:method.getGenericParameterTypes()){
            paramNameParamTypeNameMap.put(paramNames[i],type.getTypeName());
            i++;
        }
        return paramNameParamTypeNameMap;
    }

    private ApiDesc getApiDesc(Object bean, String serviceId) {
        Class serviceClass=bean.getClass();
        ApiDoc apiDoc = (ApiDoc) serviceClass.getAnnotation(ApiDoc.class);
        ApiDesc apiDesc = new ApiDesc();
        apiDesc.setApiId(serviceId);
        apiDesc.setServiceTypeFullName(serviceClass.getName());
        apiDesc.setServiceTypeSingleName(serviceClass.getSimpleName());
        apiDesc.setServiceDesc(getApiDocDesc(apiDoc));
        apiDesc.setServiceObj(bean);

        return apiDesc;
    }
    private String getApiDocDesc(ApiDoc apiDoc){
        return apiDoc == null ? "" : apiDoc.desc();
    }


}
