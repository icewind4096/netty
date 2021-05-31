package com.mindmotion.netty.http.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class NettyHttpServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        //加入netty自带的一个编码器处理器
        pipeline.addLast("HttpServerCodec", new HttpServerCodec());

        //加入自定义的一个处理器
        pipeline.addLast("MyNettyHttpServerHandler", new MyNettyHttpServerHandler());
    }
}
