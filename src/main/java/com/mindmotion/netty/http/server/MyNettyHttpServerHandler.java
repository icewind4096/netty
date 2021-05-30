package com.mindmotion.netty.http.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * 1. SimpleChannelInboundHandler是ChannelInboundHandlerAdapter的子类
 * 2. HttpObject表示客户端和服务器端相互通讯的数据被封装成httpojbect
 */
public class MyNettyHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    //读取客户端数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        //判断msg是不是一个httprequest请求
        if (msg instanceof HttpRequest){
            System.out.println("msg类型=====>" + msg.getClass());
            System.out.println("客户端IP地址=====>" + ctx.channel().remoteAddress());

            //回复给浏览器[http协议]
            ByteBuf content = Unpooled.copiedBuffer("[服务器]你好，客户端@" + ctx.channel().remoteAddress().toString(), CharsetUtil.UTF_8);

            //构造一个http的响应 httpreponse
            FullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            httpResponse.headers().add(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            httpResponse.headers().add(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            //将httpresponse返回
            ctx.writeAndFlush(httpResponse);
        }
    }
}
