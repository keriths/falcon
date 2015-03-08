package com.fs.falcon.seria;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;
import org.jboss.netty.handler.codec.serialization.ClassResolver;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;

import java.io.*;

/**
 * Created by fanshuai on 15-2-10.
 */
public class Provider1Decoder extends LengthFieldBasedFrameDecoder {

    private final ClassResolver classResolver;

    /**
     * Creates a new decoder whose maximum object size is {@code 1048576}
     * bytes.  If the size of the received object is greater than
     * {@code 1048576} bytes, a {@link java.io.StreamCorruptedException} will be
     * raised.
     *
     * @deprecated use {@link #Provider1Decoder(ClassResolver)}
     */
    @Deprecated
    public Provider1Decoder() {
        this(1048576);
    }


    /**
     * Creates a new decoder whose maximum object size is {@code 1048576}
     * bytes.  If the size of the received object is greater than
     * {@code 1048576} bytes, a {@link java.io.StreamCorruptedException} will be
     * raised.
     *
     * @param classResolver  the {@link ClassResolver} to use for this decoder
     */
    public Provider1Decoder(ClassResolver classResolver) {
        this(1048576, classResolver);
    }

    /**
     * Creates a new decoder with the specified maximum object size.
     *
     * @param maxObjectSize  the maximum byte length of the serialized object.
     *                       if the length of the received object is greater
     *                       than this value, {@link java.io.StreamCorruptedException}
     *                       will be raised.
     * @deprecated           use {@link #Provider1Decoder(int, ClassResolver)}
     */
    @Deprecated
    public Provider1Decoder(int maxObjectSize) {
        this(maxObjectSize, ClassResolvers.weakCachingResolver(null));
    }

    /**
     * Creates a new decoder with the specified maximum object size.
     *
     * @param maxObjectSize  the maximum byte length of the serialized object.
     *                       if the length of the received object is greater
     *                       than this value, {@link java.io.StreamCorruptedException}
     *                       will be raised.
     * @param classResolver    the {@link ClassResolver} which will load the class
     *                       of the serialized object
     */
    public Provider1Decoder(int maxObjectSize, ClassResolver classResolver) {
        super(maxObjectSize, 0, 4, 0, 4);
        this.classResolver = classResolver;
    }


    /**
     * Create a new decoder with the specified maximum object size and the {@link ClassLoader} wrapped in {@link ClassResolvers#weakCachingResolver(ClassLoader)}
     *
     *
     * @param maxObjectSize  the maximum byte length of the serialized object.
     *                       if the length of the received object is greater
     *                       than this value, {@link java.io.StreamCorruptedException}
     *                       will be raised.
     * @param classLoader    the the classloader to use
     * @deprecated           use {@link #(int, ClassResolver)}
     */
    @Deprecated
    public Provider1Decoder(int maxObjectSize, ClassLoader classLoader) {
        this(maxObjectSize, ClassResolvers.weakCachingResolver(classLoader));
    }

    @Override
    protected Object decode(
                ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
        try {
            System.out.print(buffer.readerIndex());
            ChannelBuffer frame = (ChannelBuffer) super.decode(ctx, channel, buffer);
            if (frame == null) {
                return null;
            }

             Object o = new ObjectInputStream(
                    new ChannelBufferInputStream(frame)).readObject();
            System.out.println("   "+buffer.readerIndex());
            return o;
        }catch (Exception e){
            e.printStackTrace();
            throw e;

        }


    }

    @Override
    protected ChannelBuffer extractFrame(ChannelBuffer buffer, int index, int length) {
        return buffer.slice(index, length);
    }

    private ChannelBuffer getFrame(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer){
        long frameLength = buffer.getUnsignedInt(buffer.readerIndex());
        int readerIndex = buffer.readerIndex();
        buffer.skipBytes(4);
        int actualFrameLength = (int)frameLength;
        ChannelBuffer frame = extractFrame(buffer, 4, actualFrameLength);
        buffer.readerIndex(readerIndex + actualFrameLength);
        return frame;
    }



}


