package com.falcon.server.netty;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.SerializerFactory;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.buffer.DynamicChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by fanshuai on 15-2-9.
 */
public class ProviderDecoder extends OneToOneDecoder {
    private final static Logger log = LoggerFactory.getLogger(ProviderDecoder.class);
    private  final byte[] LENGTH_PLACEHOLDER = new byte[4];

//    class DataBuffer{
//        Integer channelId ;
//
//    }

    private String oldBufKey = "af";
    private ChannelBuffer getAttachment(ChannelHandlerContext ctx,String oldBufKey){
        Map<String,ChannelBuffer> map = (Map)ctx.getAttachment();
        if(map==null){
            return null;
        }
        return map.remove(oldBufKey);
    }
    private void setAttachment(ChannelHandlerContext ctx,String oldBufKey,ChannelBuffer buffer,Channel channel){

        if(!(buffer instanceof DynamicChannelBuffer) || buffer.writerIndex() > 102400) {
            ChannelBuffer db = ChannelBuffers.dynamicBuffer(buffer.readableBytes() * 2, channel.getConfig().getBufferFactory());
            db.writeBytes(buffer);
            buffer = db;
        }

        Map<String,ChannelBuffer> map = (Map)ctx.getAttachment();
        if(map==null){
            synchronized (ctx){
                map = (Map)ctx.getAttachment();
                if(map==null){
                    map= new ConcurrentHashMap();
                    ctx.setAttachment(map);
                }
            }
        }
        map.put(oldBufKey,buffer);

    }

//    private Object getMsg(ChannelBuffer buf) throws Exception {
//        long frameLength=buf.getUnsignedInt(buf.readerIndex());
//        int msgLength = (int) frameLength;
//        log.info("  ****************msgLength:"+msgLength);
//        if(buf.readableBytes()-4<msgLength){
//            log.error("   ****************readableBytesSize("+buf.readableBytes()+")-4<msgLength("+msgLength+")");
//            return null;
//        }
//        buf.skipBytes(LENGTH_PLACEHOLDER.length);
//        ChannelBuffer frameBuffer=buf.slice(buf.readerIndex(), msgLength);
//        buf.readerIndex(buf.readerIndex()+msgLength);
//        return readUseHessian(new ChannelBufferInputStream(frameBuffer));
//    }
    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
        log.info("decode channel:"+channel+" msg:"+msg);
        List<Object> messages = new ArrayList<Object>();
        try {
            if (!(msg instanceof ChannelBuffer)) {
                return msg;
            }
//            ChannelBuffer oldBuf = getAttachment(ctx,oldBufKey);
//            ChannelBuffer msgBuffer = (ChannelBuffer)msg;
//            if (oldBuf==null){
//                oldBuf = msgBuffer;
//            }else {
//                oldBuf.writeBytes(msgBuffer);
//            }
//            log.info("readerIndex:"+oldBuf.readerIndex());
//            //buffer.readerIndex(0);
//            Object message = null;
//            while (true){
//                if(oldBuf.readable()){
//                    int readableBytesSize = oldBuf.readableBytes();
//                    log.info("  ****************readableBytesSize:"+readableBytesSize);
//                    if(readableBytesSize<=4){
//                        setAttachment(ctx,oldBufKey,oldBuf);
//                    }else{
//                        Object messageObj = null ;
//                        try {
//                            messageObj = getMsg(oldBuf);
//                        }catch (Exception e){
//                            log.error(e.getMessage(),e);
//                        }
//
//                        if(messageObj != null){
//                            if(messages==null){
//                                messages=new ArrayList<Object>();
//                            }
//                            messages.add(message);
//                            continue;
//                        }
//                        setAttachment(ctx,oldBufKey,oldBuf);
//                    }
//                }
//                log.info("  ****************messages:"+messages);
//                return messages;
//            }
            Object message = null;
            ChannelBuffer buffer = getAttachment(ctx,oldBufKey);
            ChannelBuffer newBuffer = (ChannelBuffer)msg;
            if(buffer==null){
                buffer = newBuffer;
            }else {
                buffer.writeBytes(newBuffer);
            }
            log.info("readerIndex:"+buffer.readerIndex());
            while (buffer.readable()){
                int readableBytesSize = buffer.readableBytes();
                log.info("  ****************readableBytesSize:"+readableBytesSize);
                if(readableBytesSize<4){
                    log.error("  ****************readableBytesSize<4");
                    setAttachment(ctx,oldBufKey,buffer,channel);
                    return null;
                }
                long frameLength=buffer.getUnsignedInt(buffer.readerIndex());
                int msgLength = (int) frameLength;
                log.info("  ****************msgLength:"+msgLength);
                if(readableBytesSize-4<msgLength){
                    log.error("   ****************readableBytesSize("+readableBytesSize+")-4<msgLength("+msgLength+")");
                    setAttachment(ctx,oldBufKey,buffer,channel);
                    return null;
                }
                buffer.skipBytes(LENGTH_PLACEHOLDER.length);
                ChannelBuffer frameBuffer=buffer.slice(buffer.readerIndex(), msgLength);
                buffer.readerIndex(buffer.readerIndex()+msgLength);
                message = readUseHessian(new ChannelBufferInputStream(frameBuffer));
                messages.add(message);
            }
            setAttachment(ctx,oldBufKey,buffer,channel);
            return messages;
        }catch (Exception e){
            log.error(e.getMessage(),e);
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
