package com.falcon.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.caucho.hessian.io.HessianInput;
import com.falcon.server.servlet.FalconRequest;
import com.falcon.server.servlet.FalconResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;

/**
 * Created by fanshuai on 18/12/16.
 */
public class HTTPInvokeClient implements InvokeClient {
    private String host;
    private int  port = 80;
    public HTTPInvokeClient(String host, int port){
        this.host = host;
        this.port = port;
    }

    @Override
    public Object doRequest(FalconRequest request, InvokerContext invokerContext) {
        InvokerContext context = CustomerManager.requestIng.get(request.getSequence());
        InvokerCallBack callBack =  context.getCallBack();
        try {
            CloseableHttpClient httpclient = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost("http://"+host+":"+String.valueOf(port)+"/api/javainvoke");
            StringEntity s = new StringEntity(JSON.toJSONString(request));
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json");//发送json数据需要设置contentType
            post.setEntity(s);
            HttpResponse res = httpclient.execute(post);
            if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(res.getEntity());// 返回json格式
                System.out.println(result);
                FalconResponse response = JSONObject.parseObject(result,FalconResponse.class);
                if (response.getRetObject()!=null){
                    String base64RetObject = (String)response.getRetObject();
                    byte[] retObjectByteArray = Base64.decodeBase64(base64RetObject);
                    ByteArrayInputStream is = new ByteArrayInputStream(retObjectByteArray);
                    HessianInput hi = new HessianInput(is);
                    Object retObj = hi.readObject();
                    response.setRetObject(retObj);
                }
                callBack.processResponse(response);
            }else {
                callBack.processFailedResponse(new RuntimeException("http retcode "+res.getStatusLine().getStatusCode()));
            }

        }catch (Exception e){
            callBack.processFailedResponse(e);
        }finally {
            CustomerManager.requestIng.remove(request.getSequence());
        }
        return null;
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public void connect() {

    }
}
