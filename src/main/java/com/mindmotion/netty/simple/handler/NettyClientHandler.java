package com.mindmotion.netty.simple.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.charset.StandardCharsets;

/*
自定义handler，必须继承netty规定好的某个HandlerAdapter
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    //异常时触发
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    //当通道有读取数据时
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("服务器回复的消息:->" + byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("服务器地址:->" + ctx.channel().remoteAddress());
    }

    //当通道就绪时
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client =>" + ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello server", CharsetUtil.UTF_8));
    }
}
