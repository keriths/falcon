package com.falcon.server.netty;

import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;
import com.falcon.client.CustomerManager;
import com.falcon.client.InvokerContext;
import com.falcon.server.servlet.FalconRequest;
import com.falcon.server.servlet.FalconResponse;
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
    private  final byte[] LENGTH_PLACEHOLDER = new byte[4];
    @Override
    protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
        ChannelBufferOutputStream bout = new ChannelBufferOutputStream(ChannelBuffers.dynamicBuffer(512, ctx.getChannel().getConfig().getBufferFactory()));
        bout.write(LENGTH_PLACEHOLDER);
        serializeRequest(bout,msg);
        ChannelBuffer channelBuffer = bout.buffer();
        int msgLength = channelBuffer.writerIndex()-LENGTH_PLACEHOLDER.length;
        channelBuffer.setInt(0,msgLength);
        return channelBuffer;
    }
    public void serializeRequest(ChannelBufferOutputStream os, Object obj) throws Exception {
        Hessian2Output h2out = new Hessian2Output(os);
        h2out.setSerializerFactory(new SerializerFactory());
        try {
            h2out.writeObject(obj);
            h2out.flush();
        } catch (Throwable t) {
            if (obj instanceof FalconResponse){
                h2out.init(os);
                ((FalconResponse) obj).setRetObject(null);
                ((FalconResponse) obj).setErrorMsg(t.getMessage());
                h2out.writeObject(obj);
                h2out.flush();
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
