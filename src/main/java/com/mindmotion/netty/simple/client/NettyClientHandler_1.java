package com.mindmotion.netty.simple.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.charset.StandardCharsets;

public class NettyClientHandler_1 extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Client->: 服务器端地址 " + ctx.channel().remoteAddress());

        //将msg转为Netty.ByteBuf
        ByteBuf byteBuf = (ByteBuf) msg;

        System.out.println("Client->: [服务器端发送数据] " + byteBuf.toString(CharsetUtil.UTF_8));
    }

    //当通道就绪时，就会触发该方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client->: 通道就绪");
        ctx.writeAndFlush(Unpooled.copiedBuffer("你好，服务器".getBytes(StandardCharsets.UTF_8)));
    }

    //捕获发送的异常，需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
    }
}
