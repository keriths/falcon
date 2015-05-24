package com.falcon.server.netty;

import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;
import com.falcon.client.CustomerManager;
import com.falcon.client.InvokerContext;
import com.falcon.server.servlet.FalconRequest;
import com.falcon.server.servlet.FalconResponse;
import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by fanshuai on 15-2-9.
 */
public class ProviderEncoder extends OneToOneEncoder {
    private final static Logger log = Logger.getLogger(ProviderEncoder.class);
    private  final byte[] LENGTH_PLACEHOLDER = new byte[4];
    @Override
    protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {

        ChannelBuffer channelBuffer=null;
        try {
            ChannelBufferOutputStream bout = new ChannelBufferOutputStream(ChannelBuffers.dynamicBuffer(512, ctx.getChannel().getConfig().getBufferFactory()));
            bout.write(LENGTH_PLACEHOLDER);
            serializeRequest(bout,msg);
            channelBuffer = bout.buffer();
            int msgLength = channelBuffer.writerIndex()-LENGTH_PLACEHOLDER.length;
            channelBuffer.setInt(0,msgLength);
            log.error(msg+" encode object success ");
            return channelBuffer;
        } catch (Exception e) {
            log.error(msg+" encode object exception ",e);
            if(msg instanceof FalconRequest){
                long seq = ((FalconRequest) msg).getSequence();
                InvokerContext invokerContext = CustomerManager.requestIng.get(seq);
                if (invokerContext!=null){
                    invokerContext.getCallBack().processFailedResponse(e);
                    CustomerManager.requestIng.remove(seq);
                }
            }
            throw new Exception(e);
        }
    }

    public void serializeRequest(ChannelBufferOutputStream os, Object obj) throws Exception {
        Hessian2Output h2out = new Hessian2Output(os);
        h2out.setSerializerFactory(new SerializerFactory());
        try {
            log.info(obj+" hessian write object");
            h2out.writeObject(obj);
            h2out.flush();
            log.info(obj+" hessian write object over ");
        } catch (Throwable t) {
            if (obj instanceof FalconResponse){
                log.info(obj+" hessian write object error rewrite ");
                h2out.init(os);
                ((FalconResponse) obj).setRetObject(null);
                ((FalconResponse) obj).setErrorMsg(t.getMessage());
                h2out.writeObject(obj);
                h2out.flush();
                log.info(obj+" hessian write object error rewrite over ");
                return ;
            }
            if(obj instanceof FalconRequest){
                long seq = ((FalconRequest) obj).getSequence();
                InvokerContext invokerContext = CustomerManager.requestIng.get(seq);
                if (invokerContext!=null){
                    invokerContext.getCallBack().processFailedResponse(t);
                    CustomerManager.requestIng.remove(seq);
                }
                throw new Exception(t);
            }
            throw new Exception(t);

        } finally {
            try {
                h2out.close();
            } catch (IOException e) {
                throw new Exception(e);
            }
        }
    }

}
