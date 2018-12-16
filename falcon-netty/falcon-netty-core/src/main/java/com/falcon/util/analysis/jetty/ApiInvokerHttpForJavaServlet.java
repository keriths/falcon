package com.falcon.util.analysis.jetty;

import com.alibaba.fastjson.JSON;
import com.caucho.hessian.io.HessianOutput;
import com.falcon.server.ServerManager;
import com.falcon.server.servlet.FalconRequest;
import com.falcon.server.servlet.FalconResponse;
import com.falcon.util.analysis.ServiceManager;
import org.apache.commons.codec.binary.Base64;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by fanshuai on 18/11/21.
 */
public class ApiInvokerHttpForJavaServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String requestBody = getRequestContentString(req);
            FalconRequest requestDTO = JSON.toJavaObject(JSON.parseObject(requestBody), FalconRequest.class);
            FalconResponse falconResponse = new FalconResponse();
            falconResponse.setSequence(requestDTO.getSequence());

            Object obj = ServiceManager.invokeServiceMethod(requestDTO, ServerManager.HTTP);// process(requestDTO.getServiceInterfaceName(),requestDTO.getServiceMethod(),requestDTO.getParameterTypeNames(),requestDTO.getParameters());
            falconResponse.setSequence(requestDTO.getSequence());
            if (obj!=null){
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                HessianOutput ho = new HessianOutput(os);
                ho.writeObject(obj);
                byte[] retByteArray = os.toByteArray();
                String retBase64Str = Base64.encodeBase64String(retByteArray);
                falconResponse.setRetObject(retBase64Str);
            }
            resp.getWriter().write(JSON.toJSONString(falconResponse));
            resp.flushBuffer();
        }catch (Exception e){
            resp.setStatus(500);
            resp.getWriter().write(JSON.toJSONString(e.getMessage()));
            resp.flushBuffer();
        }
    }
//
//    public static Object process(String serviceName,String methodName,String methodParamTypes,Object[] paramValues){
//        ServiceMethodStructureInfo methodStructureInfo = ServiceManager.getServiceMethodStructureInfo(serviceName, methodName, methodParamTypes);
//        Assert.notNull(methodStructureInfo, "not found method " + serviceName + "." + methodName + methodParamTypes);
//        Object serviceInstance = methodStructureInfo.getServiceInstance();
//        Method method = methodStructureInfo.getMethod();
//        Object[] param = getParamObjects(paramValues, methodStructureInfo);
//        try {
//            return method.invoke(serviceInstance,param);
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException(serviceName+"."+methodName+methodParamTypes+" on invoke IllegalAccessException",e);
//        } catch (InvocationTargetException e) {
//            throw new RuntimeException(serviceName+"."+methodName+methodParamTypes+" on invoke InvocationTargetException",e);
//        }
//    }
//
//    private static Object[] getParamObjects(Object[] paramValues, ServiceMethodStructureInfo methodStructureInfo) {
//        LinkedHashMap<String, ParamStructure> paramStructureLinkedHashMap = methodStructureInfo.getParamStructureMap();
//        if (CollectionUtils.isEmpty(paramStructureLinkedHashMap)){
//            return null;
//        }
//        Object[] param = new Object[paramStructureLinkedHashMap.size()];
//        int i = 0;
//        for (Map.Entry<String, ParamStructure> entry : paramStructureLinkedHashMap.entrySet()){
//            ParamStructure paramStructure = entry.getValue();
//            String paramName = paramStructure.getParamName();
//            Type type = paramStructure.getParamType();
//            Object value = paramValues[i];
//            param[i] = jsonstrToTypeObject(type, (String)value);
//            i++;
//        }
//        return param;
//    }
//    private static Object jsonstrToTypeObject(Type type, String value) {
//        Object p = null;
//        if (Strings.isNotBlank(value)){
//            if (type instanceof Class && ((Class) type).getName().equals(String.class.getName())){
//                return value;
//            }
//            p = JSON.parseObject(value, type, Feature.values());
//        }
//        return p;
//    }
//
    private String getRequestContentString(HttpServletRequest req) throws IOException{
        byte[] requestBodyBytes = getRequestContentBytes(req);
        if (requestBodyBytes==null){
            return null;
        }
        return new String(requestBodyBytes);
    }
//
    private byte[] getRequestContentBytes(HttpServletRequest req) throws IOException {
        if (req.getContentLength()==-1){
            return null;
        }
        InputStream in =  req.getInputStream();
        byte[] requestBodyBytes = new byte[req.getContentLength()];
        byte[] buffer = new byte[1024];
        int post = 0;
        int length = 0;
        while ((length = in.read(buffer))!=-1){
            for (int i = 0;i<length;i++){
                requestBodyBytes[post]=buffer[i];
                post++;
            }
        }
        return requestBodyBytes;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }
}
