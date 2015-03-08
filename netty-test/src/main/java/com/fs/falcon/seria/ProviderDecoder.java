package com.fs.falcon.seria;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.SerializerFactory;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanshuai on 15-2-9.
 */
public class ProviderDecoder extends OneToOneDecoder {
    private  final byte[] LENGTH_PLACEHOLDER = new byte[4];
    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
        List<Object> messages = new ArrayList<Object>();
        try {
            if (!(msg instanceof ChannelBuffer)) {
                return msg;
            }
            ChannelBuffer buffer = (ChannelBuffer)msg;
            Object message = null;
            while (buffer.readable()){
                if(buffer.readableBytes()<4){
                    System.out.print("  buffer.readableBytes()<4");
                    return null;
                }
                long frameLength=buffer.getUnsignedInt(buffer.readerIndex());
                int msgLength = (int) frameLength;
                if(buffer.readableBytes()-4<msgLength){
                    System.out.print("   buffer.readableBytes()-4<msgLength");
                    return null;
                }
                buffer.skipBytes(LENGTH_PLACEHOLDER.length);
                ChannelBuffer frameBuffer=buffer.slice(buffer.readerIndex(), msgLength);
                buffer.readerIndex(buffer.readerIndex()+msgLength);
                message = readUseHessian(new ChannelBufferInputStream(frameBuffer));
                messages.add(message);
            }
            return messages;
        }catch (Exception e){
            e.printStackTrace();
            throw  e;
        }

    }
    public Object readUseHessian(InputStream is) throws Exception {
        Hessian2Input h2in = new Hessian2Input(is);
        h2in.setSerializerFactory(new SerializerFactory());
        try {
            return h2in.readObject();
        } catch (Throwable t) {
            throw new Exception(t);
        } finally {
            try {
                h2in.close();
            } catch (IOException e) {
                throw new Exception(e);
            }
        }
    }
}
