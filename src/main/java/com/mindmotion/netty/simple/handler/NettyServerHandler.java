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
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    //需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    //数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello client".getBytes(StandardCharsets.UTF_8)));
    }

    //读取实际数据，读取客户端发送的消息
    //ChannelHandlerContext包含对象-》 管道(业务) 通道(数据) 地址
    //MSG 客户端发送来的数据，以object形式提供
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("服务器读取线程 =>" + Thread.currentThread().getName());

        System.out.println("server ctx" + ctx);

        //将msg转为bytebuffer
        ByteBuf byteBuf = (ByteBuf) msg;

        System.out.println("client 发送数据:=>" + byteBuf.toString(CharsetUtil.UTF_8));

        System.out.println("客户端地址" + ctx.channel().remoteAddress());
    }
}
