package com.fs.falcon.seria;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;

import static org.jboss.netty.buffer.ChannelBuffers.dynamicBuffer;

/**
 * Created by fanshuai on 15-2-10.
 */
public class Provider1Encoder  extends OneToOneEncoder {
    private static final byte[] LENGTH_PLACEHOLDER = new byte[4];



    @Override
    protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
        ChannelBufferOutputStream bout =
                new ChannelBufferOutputStream(dynamicBuffer(
                        512, ctx.getChannel().getConfig().getBufferFactory()));
        bout.write(LENGTH_PLACEHOLDER);
        ObjectOutputStream oout = new ObjectOutputStream(bout);
        oout.writeObject(msg);
        oout.flush();
        oout.close();

        ChannelBuffer encoded = bout.buffer();
        encoded.setInt(0, encoded.writerIndex() - 4);
        return encoded;
    }




}
