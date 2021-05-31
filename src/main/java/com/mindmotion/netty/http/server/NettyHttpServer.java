package com.mindmotion.netty.http.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyHttpServer {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossEventLoopGroup = new NioEventLoopGroup(1);

        EventLoopGroup workEventLoopGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossEventLoopGroup, workEventLoopGroup)
                           .channel(NioServerSocketChannel.class)
                           .childHandler(new NettyHttpServerInitializer());

            ChannelFuture channelFuture = serverBootstrap.bind(9999).sync();

            channelFuture.channel().closeFuture().sync();
        }
        finally {
            bossEventLoopGroup.shutdownGracefully();
            workEventLoopGroup.shutdownGracefully();
        }
    }
}
